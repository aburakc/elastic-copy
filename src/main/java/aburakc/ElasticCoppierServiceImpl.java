package aburakc;

import aburakc.model.bulk.BulkResponse;
import aburakc.model.scroll.Hit;
import aburakc.model.scroll.ScrollResponse;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

@Service
public class ElasticCoppierServiceImpl implements ElasticCoppierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticCoppierServiceImpl.class);

    @Value("${fromUrl}")
    private String elasticFromUrl;

    @Value("${toUrl}")
    private String elasticToUrl;

    @Value("${bulkSize:1000}")
    private Integer bulkSize;

    @Value("${scrollTime:5m}")
    private String scrollTime;

    private static final String indexStr = "{\"index\":{\"_id\":\"%s\"}}";


    private static String getHostWithPort(String url) throws MalformedURLException {
        URL aUrl = new URL(url);
        return aUrl.getProtocol() + "://" + aUrl.getAuthority();
    }

    @Override
    public void copyIndex() throws IOException {
        String scrollId = null;
        ScrollResponse scrollResponse;
        int total = 0;
        do {
            scrollResponse = scroll(scrollId, scrollTime);
            scrollId = scrollResponse.getScrollId();
            if (!CollectionUtils.isEmpty(scrollResponse.getHits().getHits())) {
                total += bulkIndex(scrollResponse);
                LOGGER.info("Total Updated Index {}", total);
            }
        } while (!CollectionUtils.isEmpty(scrollResponse.getHits().getHits()));
    }

    @Override
    public ScrollResponse scroll(String scrollId, String timeOut) throws IOException {
        String url = String.format("%s/_search?scroll=%s", elasticFromUrl, timeOut);
        if (scrollId != null) {
            url = String.format("%s/_search/scroll", getHostWithPort(elasticFromUrl));
        }

        HttpPost httpPost = new HttpPost(url);
        StringEntity jsonEntity = null;
        if (scrollId == null) {
            jsonEntity = new StringEntity(String.format("{\"size\":%s,\"sort\": [\"_doc\"]}", bulkSize), "UTF-8");
        } else {
            jsonEntity = new StringEntity(String.format("{\"scroll\" : \"%s\",\"scroll_id\" : \"%s\"}", scrollTime, scrollId), "UTF-8");
        }
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(jsonEntity);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        long b = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String jsonResponse = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
        httpClient.close();
        LOGGER.debug(jsonResponse);
        Gson gson = new Gson();
        ScrollResponse scrollResponse = gson.fromJson(jsonResponse, ScrollResponse.class);
        LOGGER.info("Scroll Total time : {}  ES time : {}, total : {}, hits : {} ", System.currentTimeMillis() - b, scrollResponse.getTook(), scrollResponse.getHits().getTotal(), CollectionUtils.isEmpty(scrollResponse.getHits().getHits()) ? "0" : scrollResponse.getHits().getHits().size());
        return scrollResponse;
    }

    @Override
    public Integer bulkIndex(ScrollResponse scrollResponse) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Hit hit : scrollResponse.getHits().getHits()) {
            sb.append(String.format(indexStr, hit.getId()));
            sb.append("\n");
            sb.append(hit.getSource().toString());
            sb.append("\n");
        }
        String output = String.format("%s/_bulk", elasticToUrl);
        HttpPut httpPut = new HttpPut(output);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        StringEntity jsonEntity = new StringEntity(sb.toString(), "UTF-8");
        httpPut.setHeader("Content-type", "application/x-ndjson");
        httpPut.setEntity(jsonEntity);
        long b = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpPut);
        String jsonResponse = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
        LOGGER.debug(jsonResponse);
        httpClient.close();

        Gson gson = new Gson();
        BulkResponse bulkResponse = gson.fromJson(jsonResponse, BulkResponse.class);
        LOGGER.info("Bulk Total time : {}, ES time : {}, error : {}, updated : {} ", System.currentTimeMillis() - b, bulkResponse.getTook(), bulkResponse.getErrors(), CollectionUtils.isEmpty(bulkResponse.getItems()) ? "0" : bulkResponse.getItems().size());
        return bulkResponse.getItems().size();
    }

}

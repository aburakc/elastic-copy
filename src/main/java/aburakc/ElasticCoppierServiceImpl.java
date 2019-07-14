package aburakc;

import aburakc.model.id.IdResponse;
import aburakc.model.mget.Doc;
import aburakc.model.mget.MGetResponse;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticCoppierServiceImpl implements ElasticCoppierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticCoppierServiceImpl.class);

    @Value("${fromUrl}")
    private String elasticFromUrl;

    @Value("${toUrl}")
    private String elasticToUrl;

    @Value("${bulkSize:100}")
    private Integer bulkSize;

    @Value("${fromIndex:0}")
    private Long fromIndex;

    @Override
    public void test() {
        LOGGER.info(elasticFromUrl);
    }

    @Override
    public void beginCopy() {
        long index = fromIndex;
        long count = getCount();
        while (index<count) {
            List<String> ids = getIds(index);
            index += ids.size();
            copyDocs(getDocs(ids));
            LOGGER.info("Total : " + index);
        }
    }

    public Long getCount() {
        IdResponse idResponse = getIdResponse(0L, 1);
        return idResponse.getHits().getTotal();
    }


    private IdResponse getIdResponse(Long fromIndex, Integer size) {
        //http://localhost:9200/companydatabase/scroll/_search?stored_fields=id&size=1000&from=5000

        String output = String.format("%s/_search?stored_fields=id&size=%d&from=%d", elasticFromUrl, size, fromIndex);

        HttpGetWithEntity httpGet = new HttpGetWithEntity(output);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String json = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.debug(json);
            httpClient.close();
            Gson gson = new Gson();
            IdResponse idResponse = gson.fromJson(json, IdResponse.class);
            return idResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public List<String> getIds(Long fromIndex) {
        IdResponse idResponse = getIdResponse(fromIndex, bulkSize);
        return idResponse.getHits().getHits().stream().map(p -> p.getId()).collect(Collectors.toList());
    }


    @Override
    public List<Doc> getDocs(List<String> ids) {
        //http://localhost:9200/companydatabase/employees/_mget

        String output = String.format("%s/_mget", elasticFromUrl);

        HttpGetWithEntity httpGet = new HttpGetWithEntity(output);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();
            String requestBody = "{\"ids\":" + gson.toJson(ids) + "}";

            StringEntity jsonEntity = new StringEntity(requestBody, "UTF-8");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setEntity(jsonEntity);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String jsonResponse = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.debug(jsonResponse);
            MGetResponse mGet = gson.fromJson(jsonResponse, MGetResponse.class);
            httpClient.close();
            return mGet.getDocs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    public void copyDocs(List<Doc> docs) {

        String indexStr = "{\"index\":{\"_id\":\"%s\"}}";
        StringBuilder sb = new StringBuilder();

        for (Doc d : docs) {
            sb.append(String.format(indexStr, d.getId()));
            sb.append("\n");
            sb.append(d.getSource().toString());
            sb.append("\n");
        }

        LOGGER.debug(sb.toString());

        String output = String.format("%s/_bulk", elasticToUrl);


        HttpPut httpPut = new HttpPut(output);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            StringEntity jsonEntity = new StringEntity(sb.toString(), "UTF-8");
            httpPut.setEntity(jsonEntity);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            String jsonResponse = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.debug(jsonResponse);
            httpClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

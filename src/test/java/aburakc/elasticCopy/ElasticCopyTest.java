package aburakc.elasticCopy;

import aburakc.ElasticCoppierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest(properties = {"fromUrl=http://localhost:9200/companydatabase/employees","toUrl=http://localhost:9200/companydatabase/employees", "bulkSize:1"})
@RunWith(SpringRunner.class)
public class ElasticCopyTest {

    @Autowired
   private ElasticCoppierService elasticCoppierService;


    @Test
    public void copyIndexTest() throws IOException {
       //elasticCoppierService.copyIndex();
    }

    @Test
    public void urlTest() throws MalformedURLException {
        URL aURL = new URL("http://example.com:80/docs/books/tutorial"
                + "/index.html?name=networking#DOWNLOADING");

        System.out.println("protocol = " + aURL.getProtocol());
        System.out.println("authority = " + aURL.getAuthority());
        System.out.println("host = " + aURL.getHost());
        System.out.println("port = " + aURL.getPort());
        System.out.println("path = " + aURL.getPath());
        System.out.println("query = " + aURL.getQuery());
        System.out.println("filename = " + aURL.getFile());
        System.out.println("ref = " + aURL.getRef());

    }

}

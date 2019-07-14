package aburakc.elasticCopy;

import aburakc.ElasticCoppierService;
import aburakc.model.mget.Doc;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(properties = {"fromUrl=http://localhost:9200/companydatabase/employees","toUrl=http://localhost:9200/companydatabase/employees", "bulkSize:1"})
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
   private ElasticCoppierService elasticCoppierService;


    @org.junit.Test
    public void getIdsTest() {
        List<String> ids = elasticCoppierService.getIds(0L);
        Assert.assertEquals(100, ids.size());
    }

    @org.junit.Test
    public void copyTest() {
        List<String> ids = elasticCoppierService.getIds(0L);
        List<Doc> docs = elasticCoppierService.getDocs(ids);
        elasticCoppierService.copyDocs(docs);
    }

}

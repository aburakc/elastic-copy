package aburakc;

import aburakc.model.mget.Doc;

import java.util.List;

public interface ElasticCoppierService {

    void test();

    void beginCopy();

    List<String> getIds(Long fromIndex);

    List<Doc> getDocs(List<String> ids);

    void copyDocs(List<Doc> docs);
}

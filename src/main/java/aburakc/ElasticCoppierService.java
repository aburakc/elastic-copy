package aburakc;

import aburakc.model.scroll.ScrollResponse;

import java.io.IOException;

public interface ElasticCoppierService {

    void copyIndex() throws IOException;

    ScrollResponse scroll(String scrollId, String timeOut) throws IOException;

    Integer bulkIndex(ScrollResponse scrollResponse) throws IOException;
}

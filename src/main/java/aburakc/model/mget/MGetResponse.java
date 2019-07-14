package aburakc.model.mget;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MGetResponse {

@SerializedName("docs")
@Expose
private List<Doc> docs = null;

public List<Doc> getDocs() {
return docs;
}

public void setDocs(List<Doc> docs) {
this.docs = docs;
}

}
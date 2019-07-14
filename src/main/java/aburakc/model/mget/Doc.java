package aburakc.model.mget;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc {

@SerializedName("_index")
@Expose
private String index;
@SerializedName("_type")
@Expose
private String type;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("_version")
@Expose
private Integer version;
@SerializedName("found")
@Expose
private Boolean found;
@SerializedName("_source")
@Expose
private JsonElement source;

public String getIndex() {
return index;
}

public void setIndex(String index) {
this.index = index;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public Integer getVersion() {
return version;
}

public void setVersion(Integer version) {
this.version = version;
}

public Boolean getFound() {
return found;
}

public void setFound(Boolean found) {
this.found = found;
}

public JsonElement getSource() {
return source;
}

public void setSource(JsonElement source) {
this.source = source;
}

}
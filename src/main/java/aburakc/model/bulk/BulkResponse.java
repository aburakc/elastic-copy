package aburakc.model.bulk;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulkResponse {

@SerializedName("took")
@Expose
private Integer took;
@SerializedName("errors")
@Expose
private Boolean errors;
@SerializedName("items")
@Expose
private List<Item> items = null;

public Integer getTook() {
return took;
}

public void setTook(Integer took) {
this.took = took;
}

public Boolean getErrors() {
return errors;
}

public void setErrors(Boolean errors) {
this.errors = errors;
}

public List<Item> getItems() {
return items;
}

public void setItems(List<Item> items) {
this.items = items;
}

}
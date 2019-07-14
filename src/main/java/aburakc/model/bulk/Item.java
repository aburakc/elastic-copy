package aburakc.model.bulk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

@SerializedName("index")
@Expose
private Index index;

public Index getIndex() {
return index;
}

public void setIndex(Index index) {
this.index = index;
}

}

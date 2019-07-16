package aburakc.model.scroll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScrollResponse {

@SerializedName("_scroll_id")
@Expose
private String scrollId;
@SerializedName("took")
@Expose
private Long took;
@SerializedName("timed_out")
@Expose
private Boolean timedOut;
@SerializedName("_shards")
@Expose
private Shards shards;
@SerializedName("hits")
@Expose
private Hits hits;

public String getScrollId() {
return scrollId;
}

public void setScrollId(String scrollId) {
this.scrollId = scrollId;
}

public Long getTook() {
return took;
}

public void setTook(Long took) {
this.took = took;
}

public Boolean getTimedOut() {
return timedOut;
}

public void setTimedOut(Boolean timedOut) {
this.timedOut = timedOut;
}

public Shards getShards() {
return shards;
}

public void setShards(Shards shards) {
this.shards = shards;
}

public Hits getHits() {
return hits;
}

public void setHits(Hits hits) {
this.hits = hits;
}

}

package aburakc.model.scroll;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hits {

@SerializedName("total")
@Expose
private Long total;
@SerializedName("max_score")
@Expose
private Double maxScore;
@SerializedName("hits")
@Expose
private List<Hit> hits = null;

public Long getTotal() {
return total;
}

public void setTotal(Long total) {
this.total = total;
}

public Double getMaxScore() {
return maxScore;
}

public void setMaxScore(Double maxScore) {
this.maxScore = maxScore;
}

public List<Hit> getHits() {
return hits;
}

public void setHits(List<Hit> hits) {
this.hits = hits;
}

}
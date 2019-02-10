package indexer;

import java.util.Comparator;

/**
 * Created by Pavel on 07.02.2019.
 */
public class WebPageVector implements Comparator<WebPageVector> {
    private String url;
    private Double[] vector;
    private Double weight;

    @Override
    public int compare(WebPageVector o1, WebPageVector o2) {
        Double result = o2.weight - o1.weight;
        return (result < 0) ? -1 : ((result > 0) ? 1 : 0);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}

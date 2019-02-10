package ranker;

/**
 * Created by Pavel on 08.02.2019.
 */
public class WebPageWeight implements Comparable<WebPageWeight> {
    private String title;
    private String url;
    private Double weight;
    private String shortText;

    @Override
    public int compareTo(WebPageWeight o) {
        Double result = o.weight-this.weight;
        return (result<0)?-1:((result>0)?1:0);
    }

    @Override
    public String toString() {
        return "ranker.Ranker.ranker.WebPageWeight{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", weight=" + weight +
                ", shortText='" + shortText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPageWeight that = (WebPageWeight) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        return shortText != null ? shortText.equals(that.shortText) : that.shortText == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (shortText != null ? shortText.hashCode() : 0);
        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }
}

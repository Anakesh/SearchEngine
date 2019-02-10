package databaseComunication.entity;


import javax.persistence.*;

@Entity
@Table(name = "website_to_crawl")
public class WebsiteToCrawl {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "URL")
    private String url;

    @Override
    public String toString() {
        return "WebsiteToCrawl{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteToCrawl that = (WebsiteToCrawl) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

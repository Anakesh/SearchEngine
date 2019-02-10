package databaseComunication.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "web_page")
public class WebPage {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "URL", nullable = false, unique = true)
    private String url;
    @Column(name = "TITLE",nullable = false)
    private String title;
    @Column(name = "TEXT",nullable = false)
    @Type(type = "text")
    private String text;
    @Column(name = "crc", nullable = false)
    private String crc;
    @Column(name = "INDEXED",nullable = false)
    private Boolean indexed;
    @Column(name = "DATETIME")
    private LocalDateTime indexDate;

    @OneToMany(mappedBy = "webPage")
    public Set<WordWeight> wordWeightSet = new HashSet<>();

    @Override
    public String toString() {
        return "WebPage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
//                ", text='" + text + '\'' +
                ", crc='" + crc + '\'' +
                ", indexed=" + indexed +
                ", indexDate=" + indexDate +
//                ", wordWeightSet=" + wordWeightSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPage webPage = (WebPage) o;

        if (id != null ? !id.equals(webPage.id) : webPage.id != null) return false;
        if (url != null ? !url.equals(webPage.url) : webPage.url != null) return false;
        if (title != null ? !title.equals(webPage.title) : webPage.title != null) return false;
        if (text != null ? !text.equals(webPage.text) : webPage.text != null) return false;
        if (crc != null ? !crc.equals(webPage.crc) : webPage.crc != null) return false;
        if (indexed != null ? !indexed.equals(webPage.indexed) : webPage.indexed != null) return false;
        return indexDate != null ? indexDate.equals(webPage.indexDate) : webPage.indexDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (crc != null ? crc.hashCode() : 0);
        result = 31 * result + (indexed != null ? indexed.hashCode() : 0);
        result = 31 * result + (indexDate != null ? indexDate.hashCode() : 0);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public LocalDateTime getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(LocalDateTime indexDate) {
        this.indexDate = indexDate;
    }

    public Set<WordWeight> getWordWeightSet() {
        return wordWeightSet;
    }

    public void setWordWeightSet(Set<WordWeight> wordWeightSet) {
        this.wordWeightSet = wordWeightSet;
    }
}

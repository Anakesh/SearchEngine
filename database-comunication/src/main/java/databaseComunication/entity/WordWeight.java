package databaseComunication.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pavel on 05.02.2019.
 */

@Entity
@Table(name = "word_weight")
public class WordWeight implements Cloneable {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WORD_ID")
    private Word word;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAGE_ID")
    private WebPage webPage;

    @Column(name = "TF")
    private Double tf;

    @Column(name = "TF_IDF")
    private Double tfIdf;

    @OneToMany(mappedBy = "wordWeight")
    private Set<WordPosition> wordPositionSet = new HashSet<>();


    @Override
    public String toString() {
        return "WordWeight{" +
                "id=" + id +
                ", word=" + word +
                ", webPage=" + webPage +
                ", tf=" + tf +
                ", tfIdf=" + tfIdf +
//                ", wordPositionSet=" + wordPositionSet +
                '}';
    }

    @Override
    public WordWeight clone() throws CloneNotSupportedException {
        return (WordWeight) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordWeight that = (WordWeight) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        if (webPage != null ? !webPage.equals(that.webPage) : that.webPage != null) return false;
        if (tf != null ? !tf.equals(that.tf) : that.tf != null) return false;
        return tfIdf != null ? tfIdf.equals(that.tfIdf) : that.tfIdf == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (webPage != null ? webPage.hashCode() : 0);
        result = 31 * result + (tf != null ? tf.hashCode() : 0);
        result = 31 * result + (tfIdf != null ? tfIdf.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public Double getTf() {
        return tf;
    }

    public void setTf(Double tf) {
        this.tf = tf;
    }

    public Double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(Double tfIdf) {
        this.tfIdf = tfIdf;
    }

    public Set<WordPosition> getWordPositionSet() {
        return wordPositionSet;
    }

    public void setWordPositionSet(Set<WordPosition> wordPositionSet) {
        this.wordPositionSet = wordPositionSet;
    }
}

package databaseComunication.entity;

import javax.persistence.*;

/**
 * Created by Pavel on 08.02.2019.
 */

@Entity
@Table(name = "word_position")
public class WordPosition {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    protected Long id;
    @Column(name = "POSITION")
    protected Integer position;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WORD_WEIGHT_ID")
    private WordWeight wordWeight;

    @Override
    public String toString() {
        return "WordPosition{" +
                "id=" + id +
                ", position=" + position +
                ", wordWeight=" + wordWeight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordPosition that = (WordPosition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        return wordWeight != null ? wordWeight.equals(that.wordWeight) : that.wordWeight == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (wordWeight != null ? wordWeight.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public WordWeight getWordWeight() {
        return wordWeight;
    }

    public void setWordWeight(WordWeight wordWeight) {
        this.wordWeight = wordWeight;
    }
}

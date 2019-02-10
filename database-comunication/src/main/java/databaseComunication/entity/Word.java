package databaseComunication.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pavel on 02.02.2019.
 */
@Entity
@Table(name = "word")
public class Word {
        @Id
        @GeneratedValue
        @Column(name = "ID")
        protected Long id;
        @Column(name = "WORD", nullable = false,unique = true)
        protected String word;

        @OneToMany(mappedBy = "word")
        protected Set<WordWeight> wordWeightSet = new HashSet<>();


        @Override
        public String toString() {
                return "Word{" +
                        "id=" + id +
                        ", word='" + word + '\'' +
//                        ", wordWeightSet=" + wordWeightSet +
                        '}';
        }


        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Word word1 = (Word) o;

                if (id != null ? !id.equals(word1.id) : word1.id != null) return false;
                return word != null ? word.equals(word1.word) : word1.word == null;
        }

        @Override
        public int hashCode() {
                int result = id != null ? id.hashCode() : 0;
                result = 31 * result + (word != null ? word.hashCode() : 0);
                return result;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getWord() {
                return word;
        }

        public void setWord(String word) {
                this.word = word;
        }

        public Set<WordWeight> getWordWeightSet() {
                return wordWeightSet;
        }

        public void setWordWeightSet(Set<WordWeight> wordWeightSet) {
                this.wordWeightSet = wordWeightSet;
        }
}

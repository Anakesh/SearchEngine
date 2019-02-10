package databaseComunication;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordWeight;
import org.hibernate.SessionFactory;
import databaseComunication.repository.WebPageRep;
import databaseComunication.repository.WordPositionRep;
import databaseComunication.repository.WordRep;
import databaseComunication.repository.WordWeightRep;

import java.util.List;

/**
 * Created by Pavel on 07.02.2019.
 */
public class DatabaseRankerCom {
    private WebPageRep webPageRep;
    private WordRep wordRep;
    private WordWeightRep wordWeightRep;
    private WordPositionRep wordPositionRep;

    public DatabaseRankerCom(DatabaseConnect databaseConnect){
        SessionFactory sessionFactory = databaseConnect.getSessionFactory();
        this.webPageRep = new WebPageRep(sessionFactory);
        this.wordRep = new WordRep(sessionFactory);
        this.wordWeightRep = new WordWeightRep(sessionFactory);
        this.wordPositionRep = new WordPositionRep(sessionFactory);
    }
    public Word getWord(String inWord) {
        if(wordRep.exists(inWord)){
            return wordRep.getByStrWord(inWord);
        } else
            return null;
    }

    public List<WordWeight> getWordWeights(Word word) {
        return wordWeightRep.getAllByWord(word);
    }

    public WebPage getWebPage(Long webPageId) {
        return webPageRep.getById(webPageId);
    }
    public List<WordWeight> getAllContainingWords(List<Word> words){ return wordWeightRep.getAllContainingWords(words);}
}

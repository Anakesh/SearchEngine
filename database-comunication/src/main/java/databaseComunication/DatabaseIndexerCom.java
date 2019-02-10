package databaseComunication;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordPosition;
import databaseComunication.entity.WordWeight;
import org.hibernate.SessionFactory;
import databaseComunication.repository.WebPageRep;
import databaseComunication.repository.WordPositionRep;
import databaseComunication.repository.WordRep;
import databaseComunication.repository.WordWeightRep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 07.02.2019.
 */
public class DatabaseIndexerCom {
    private WebPageRep webPageRep;
    private WordRep wordRep;
    private WordWeightRep wordWeightRep;
    private WordPositionRep wordPositionRep;

    public DatabaseIndexerCom(DatabaseConnect databaseConnect){
        SessionFactory sessionFactory = databaseConnect.getSessionFactory();
        this.webPageRep = new WebPageRep(sessionFactory);
        this.wordRep = new WordRep(sessionFactory);
        this.wordWeightRep = new WordWeightRep(sessionFactory);
        this.wordPositionRep = new WordPositionRep(sessionFactory);
    }

    public void addWordWeightPosList(List<WordWeight> wordWeightList) {
        wordWeightRep.addList(wordWeightList);
    }

    public void updateWebPage(WebPage webPage) {
        webPageRep.update(webPage);
    }

    public Word getWordEntity(String word) {
        return wordRep.getByStrWord(word);
    }

    public List<Word> getAllWords() {
        List<Word> wordList = wordRep.readAll();
        if(wordList==null)
            return new ArrayList<>();
        else
            return wordList;
    }

    public List<WordWeight> getWWPbyWord(Word word) {
        return wordWeightRep.getAllByWord(word);
    }

    public Long getPageCount() {
        return webPageRep.count();
    }

    public Long getNumOfPagesWithWord(Word word) {
        return wordWeightRep.getNumOfPagesWithWord(word);
    }

    public List<WebPage> getPagesToIndex(int numOfWebPages) {
        return webPageRep.getNotIndexed(numOfWebPages);
    }

    public void updateTfIdf(WordWeight wwp) {
        wordWeightRep.updateTfIdf(wwp);
    }

    public boolean wordExists(String strWord) {
        return wordRep.exists(strWord);
    }

    public void addWordIfNotExist(Word word) {
        wordRep.addIfNotExists(word);
    }

    public void addWordWeight(WordWeight wordWeight) {
        wordWeightRep.addIfNotExist(wordWeight);
    }

    public WordWeight getWordWeight(WordWeight wordWeight) {
        return wordWeightRep.getByPageWord(wordWeight.getWebPage(),wordWeight.getWord());
    }

    public void addWordPosition(WordPosition wordPosition) {
        wordPositionRep.add(wordPosition);
    }
}

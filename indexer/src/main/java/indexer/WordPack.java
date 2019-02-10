package indexer;

import databaseComunication.entity.WebPage;

import java.util.List;
import java.util.Map;

/**
 * Created by Pavel on 07.02.2019.
 */
public class WordPack {
    private Map<String,List<Integer>> wordMap;
    private Integer numOfWords;
    private WebPage webPageToIndex;

    public WordPack(Map<String, List<Integer>> wordMap, Integer numOfWords, WebPage webPageToIndex) {
        this.wordMap = wordMap;
        this.numOfWords = numOfWords;
        this.webPageToIndex = webPageToIndex;
    }

    public Map<String, List<Integer>> getWordMap() {
        return wordMap;
    }

    public void setWordMap(Map<String, List<Integer>> wordMap) {
        this.wordMap = wordMap;
    }

    public Integer getNumOfWords() {
        return numOfWords;
    }

    public void setNumOfWords(Integer numOfWords) {
        this.numOfWords = numOfWords;
    }

    public WebPage getWebPageToIndex() {
        return webPageToIndex;
    }

    public void setWebPageToIndex(WebPage webPageToIndex) {
        this.webPageToIndex = webPageToIndex;
    }
}

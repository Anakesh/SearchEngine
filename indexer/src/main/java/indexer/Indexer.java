package indexer;

import databaseComunication.DatabaseIndexerCom;
import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordPosition;
import databaseComunication.entity.WordWeight;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;


public class Indexer implements Callable<Boolean>{
    private WebPage webPage;
    private DatabaseIndexerCom databaseIndexerCom;
    private int numOfWords;

    public Indexer(WebPage webPage, DatabaseIndexerCom databaseIndexerCom) {
        this.webPage = webPage;
        this.databaseIndexerCom = databaseIndexerCom;
    }

    @Override
    public Boolean call() throws Exception {
        try{
            String threadName = Thread.currentThread().getName();
            WordPack wordPack = parseText(webPage);
            System.out.println(threadName+"\twordpack");
            addNewWords(wordPack.getWordMap().keySet());
            System.out.println(threadName+"\tadded words");
            addWeightsPos(wordPack);
            System.out.println(threadName+"\tadded word_weight_pos");
            webPage.setIndexed(true);
            webPage.setIndexDate(LocalDateTime.now());
            databaseIndexerCom.updateWebPage(webPage);
            System.out.println(threadName+"\tUpdated web page");
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private void addNewWords(Set<String> words) {
        for (String strWord :
                words) {
//            if (!databaseIndexerCom.wordExists(strWord)) {
                Word word = new Word();
                word.setWord(strWord);
                databaseIndexerCom.addWordIfNotExist(word);
//            }
        }
    }

    public WordPack parseText(WebPage webPage){
        Map<String,List<Integer>> wordMap = new HashMap<>();
        String[] sentences = webPage.getText().toLowerCase().replaceAll("[^a-zа-я.?!]"," ")
                .replaceAll("\\d"," ")
                .replaceAll("[\n\t]",".")
                .replaceAll("\\s+"," ")
                .split("[.?!]+");
        int currPos = 0;
        for(String sentence:sentences){
            if(!sentence.trim().isEmpty()) {
                currPos++;
                String[] words = sentence.trim().split("\\s+");
                for (String word : words) {
                    wordMap.computeIfAbsent(word, (v) -> new ArrayList<>());
                    wordMap.get(word).add(currPos);
                    currPos++;
                }
            }
        }
        numOfWords = currPos;
        if(wordMap.isEmpty())
            return null;
        else{
            return new WordPack(wordMap,numOfWords,webPage);
        }
    }

    private void addWeightsPos(WordPack wordPack){
        for(Map.Entry<String,List<Integer>> entry : wordPack.getWordMap().entrySet()){
            Word word = databaseIndexerCom.getWordEntity(entry.getKey());
            Double tf = ((double) entry.getValue().size())/ wordPack.getNumOfWords();

            WordWeight wordWeight = new WordWeight();
            wordWeight.setWord(word);
            wordWeight.setWebPage(webPage);
            wordWeight.setTf(tf);
            wordWeight.setTfIdf(0d);
            databaseIndexerCom.addWordWeight(wordWeight);
            WordWeight wordWeightWithId = databaseIndexerCom.getWordWeight(wordWeight);
            for(Integer pos:entry.getValue()) {
                WordPosition wordPosition = new WordPosition();
                wordPosition.setPosition(pos);
                wordPosition.setWordWeight(wordWeightWithId);
                databaseIndexerCom.addWordPosition(wordPosition);
            }
        }
    }
}


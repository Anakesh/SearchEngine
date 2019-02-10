package ranker;

import databaseComunication.DatabaseConnect;
import databaseComunication.DatabaseRankerCom;
import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordPosition;
import databaseComunication.entity.WordWeight;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 07.02.2019.
 */
public class Ranker {
    private DatabaseRankerCom databaseRankerCom;

    public Ranker() {
        this.databaseRankerCom = new DatabaseRankerCom(new DatabaseConnect());
    }

    public List<WebPageWeight> findWebPagesByWord(String inStrWord){
        Word word = databaseRankerCom.getWord(inStrWord);
        if(word==null)
            return new ArrayList<WebPageWeight>();
        Pattern firstOcurPattern = generateFirstOcurPatten(inStrWord);
        List<WordWeight> wordWeights = databaseRankerCom.getWordWeights(word);
        List<WebPageWeight> webPageWeights = new ArrayList<>();
        for(WordWeight wordWeight:wordWeights){
            WebPageWeight webPageWeight = new WebPageWeight();
            webPageWeight.setUrl(wordWeight.getWebPage().getUrl());
            webPageWeight.setTitle(wordWeight.getWebPage().getTitle());
            Matcher matcher = firstOcurPattern.matcher(wordWeight.getWebPage().getText());
            if(matcher.find()){
                webPageWeight.setShortText("..."+matcher.group()+"...");
            }
            else
                webPageWeight.setShortText("\n");
            webPageWeight.setWeight(wordWeight.getTfIdf());
            webPageWeights.add(webPageWeight);
        }

        return webPageWeights.stream().sorted().collect(Collectors.toList());
    }

    public List<WebPageWeight> findWebPagesByWords(List<String> inStrWords){
        List<Word> words = getWords(inStrWords);
        if(words.isEmpty())
            return new ArrayList<WebPageWeight>();

        Pattern firstOcurPattern = generateFirstOcurPatten(words.get(0).getWord());
        for(int i = 1;i<words.size();i++){
            firstOcurPattern = Pattern.compile(firstOcurPattern.pattern()+"|"+generateFirstOcurPatten(words.get(i).getWord()));
        }
        int numOfWords = words.size();

        Map<WebPage,Double[]> vectors = new HashMap<>();
        int currWordNum = 0;
        for(Word word:words){
            List<WordWeight> wordWeights = databaseRankerCom.getWordWeights(word);
            for(WordWeight wordWeight:wordWeights){
                WebPage webPage = wordWeight.getWebPage();
                Double wordTfIdf = wordWeight.getTfIdf();
                if(vectors.containsKey(webPage)){
                    vectors.get(webPage)[currWordNum] = wordTfIdf;
                } else{
                    Double[] wordVector = new Double[numOfWords];
                    Arrays.fill(wordVector,0.0d);
                    wordVector[currWordNum] = wordTfIdf;
                    vectors.put(webPage,wordVector);
                }
            }
            currWordNum++;
        }
        List<WebPageWeight> webPageTopMultiTfIdf = new ArrayList<>();
        List<WebPageWeight> webPageTopSingleTfIdf = new ArrayList<>();

        for(Map.Entry<WebPage,Double[]> entry:vectors.entrySet()){
            WebPageWeight webPageWeight = new WebPageWeight();
            webPageWeight.setUrl(entry.getKey().getUrl());
            webPageWeight.setTitle(entry.getKey().getTitle());
            Matcher matcher = firstOcurPattern.matcher(entry.getKey().getText());
            if(matcher.find())
                webPageWeight.setShortText("..."+matcher.group()+"...");
            else
                webPageWeight.setShortText("\n");
            List<Double> vector = new ArrayList<>(Arrays.asList(entry.getValue()));
            while(vector.remove(0.0d));
            if(vector.size()==1) {
                webPageWeight.setWeight(vector.get(0));
                webPageTopSingleTfIdf.add(webPageWeight);
            } else if(vector.size()>1) {
                webPageWeight.setWeight(calculateCosineSimilarity(entry.getValue()));
                webPageTopMultiTfIdf.add(webPageWeight);
            }
        }
        webPageTopMultiTfIdf = webPageTopMultiTfIdf.stream().sorted().collect(Collectors.toList());
        webPageTopSingleTfIdf = webPageTopSingleTfIdf.stream().sorted().collect(Collectors.toList());
        webPageTopMultiTfIdf.addAll(webPageTopSingleTfIdf);
        return webPageTopMultiTfIdf;
    }

    private Double calculateCosineSimilarity(Double[] vector){
        Double[] targetVector = new Double[vector.length];
        Arrays.fill(targetVector,2d);
        double numerator = 0d;
        double denominatorTarget = 0d;
        double denominatorInput = 0d;
        for (int i =0;i<vector.length;i++){
            numerator+=vector[i]*targetVector[i];
            denominatorInput +=vector[i]*vector[i];
            denominatorTarget+=targetVector[i]*targetVector[i];
        }
        double result = numerator/(Math.sqrt(denominatorInput)*Math.sqrt(denominatorTarget));
        if(Double.isNaN(result)){
            return 0d;
        } else{
            return result;
        }
    }

    public List<WebPageWeight> findWebPagesBySentence(List<String> sentenceWords){
        List<Word> words = getWords(sentenceWords);
        if(words.size()!=sentenceWords.size())
            return new ArrayList<WebPageWeight>();
        List<WordWeight> wordWeights = databaseRankerCom.getAllContainingWords(words);
        Pattern sentenceOcurPattern = Pattern.compile("(?![\\n\\t\\f\\r]).{0,200}[^a-zA-Zа-яА-Я](?iu:"+sentenceWords.get(0)+")");
        for(int i=1;i<sentenceWords.size();i++){
            sentenceOcurPattern = Pattern.compile(sentenceOcurPattern.pattern()+"[^a-zA-Zа-яА-Я\n\t\f\r]{1,5}(?iu:"+sentenceWords.get(i)+")");
        }
        sentenceOcurPattern = Pattern.compile(sentenceOcurPattern.pattern()+"[^a-zA-zа-яА-я].{0,200}(?![\\n\\t\\f\\r])");
        Map<WebPage,Map<Word, WordWeight>> webPageListMap =
                wordWeights.stream().
                        collect(Collectors.groupingBy(WordWeight::getWebPage)).entrySet().stream().
                        collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry ->entry.getValue().stream()
                                        .collect(Collectors.toMap(WordWeight::getWord,Function.identity()))));
        List<WebPageWeight> topWebPages = new ArrayList<>();
        for(Map.Entry<WebPage,Map<Word, WordWeight>> entry:webPageListMap.entrySet()){
            boolean ok = true;
            List<Integer> basePos = entry.getValue().get(words.get(0)).getWordPositionSet().stream().map(WordPosition::getPosition).collect(Collectors.toList());
            for(int i =1;i<words.size();i++){
                List<Integer> currPos = entry.getValue().get(words.get(i)).getWordPositionSet().stream().map(WordPosition::getPosition).collect(Collectors.toList());
                for(int j = 0;j<currPos.size();j++){
                    currPos.set(j,currPos.get(j)-i);
                }
                basePos.retainAll(currPos);
                if(basePos.size()<1){
                    ok = false;
                    break;
                }
            }
            if(ok){
                Double[] vector = new Double[words.size()];
                for(int i =0;i<words.size();i++){
                    vector[i] = entry.getValue().get(words.get(i)).getTfIdf();
                }
                WebPageWeight webPageWeight = new WebPageWeight();
                webPageWeight.setUrl(entry.getKey().getUrl());
                webPageWeight.setTitle(entry.getKey().getTitle());
                Matcher matcher = sentenceOcurPattern.matcher(entry.getKey().getText());
                if(matcher.find())
                    webPageWeight.setShortText("..."+matcher.group()+"...");
                else
                    webPageWeight.setShortText("\n");
                webPageWeight.setWeight(calculateCosineSimilarity(vector));
                topWebPages.add(webPageWeight);
            }
        }
        return topWebPages.stream().sorted().collect(Collectors.toList());
    }

    private List<Word> getWords(List<String> strWords){
        List<Word> words = new ArrayList<>();
        for (String inWord :strWords){
            Word word = databaseRankerCom.getWord(inWord);
            if(word!= null)
                words.add(word);
        }
        return words;
    }
    private Pattern generateFirstOcurPatten(String word){
        return Pattern.compile("((?![\\n\\t\\f\\r]).{0,200}[^a-zA-Zа-яА-Я](?iu:"+word+")[^a-zA-zа-яА-я].{0,200}(?![\\n\\t\\f\\r]))");
    }
}

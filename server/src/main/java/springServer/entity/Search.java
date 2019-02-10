package springServer.entity;

import databaseComunication.DatabaseConnect;
import databaseComunication.DatabaseRankerCom;
import ranker.Ranker;
import ranker.WebPageWeight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {
    private final static Ranker ranker = new Ranker();
    private String text;
    private List<WebPageWeight> answers;
    private boolean empty = false;

    public void findResult(){
        List<String> words = Arrays.asList(text.toLowerCase().replaceAll("[^a-zа-я]"," ")
                .replaceAll("\\d"," ")
                .replaceAll("\\s+"," ")
                .trim()
                .split(" "));
        answers = new ArrayList<>();
        if(words.size()>1){
            answers.addAll(ranker.findWebPagesBySentence(words));
            answers.addAll(ranker.findWebPagesByWords(words));
        } else{
            answers.addAll(ranker.findWebPagesByWord(words.get(0)));
        }
        if(answers.size()>20){
            answers = answers.subList(0,20);
        } else if(answers.isEmpty()){
            empty = true;
        }
    }

    @Override
    public String toString() {
        return "Search{" +
                "text='" + text + '\'' +
                ", answers='" + answers + '\'' +
                '}';
    }

    public static Ranker getRanker() {
        return ranker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<WebPageWeight> getAnswers() {
        return answers;
    }

    public void setAnswers(List<WebPageWeight> answers) {
        this.answers = answers;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}

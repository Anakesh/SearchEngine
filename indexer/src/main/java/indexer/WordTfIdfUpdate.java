package indexer;

import databaseComunication.DatabaseIndexerCom;
import databaseComunication.entity.Word;
import databaseComunication.entity.WordWeight;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Pavel on 08.02.2019.
 */
public class WordTfIdfUpdate implements Callable<Boolean> {
    private DatabaseIndexerCom databaseIndexerCom;
    private Word word;

    public WordTfIdfUpdate(DatabaseIndexerCom databaseIndexerCom, Word word) {
        this.databaseIndexerCom = databaseIndexerCom;
        this.word = word;
    }


    @Override
    public Boolean call() throws Exception {
        Double idf = getIdf(word);
        List<WordWeight> wordWeightList = databaseIndexerCom.getWWPbyWord(word);
        for (WordWeight wwp : wordWeightList){
            Double tfIdf = wwp.getTf()*idf;
            if(tfIdf<0) {
                System.out.println(tfIdf + "\t" + wwp);
                System.out.println();
            }
            wwp.setTfIdf(tfIdf);
            databaseIndexerCom.updateTfIdf(wwp);
        }
        return true;
    }

    private Double getIdf(Word word){
        double pageCount = (double)databaseIndexerCom.getPageCount();
        double numOfPagesWithWord = (double)databaseIndexerCom.getNumOfPagesWithWord(word);
        double idf = 0d;
        if(numOfPagesWithWord>0)
            idf = Math.log(pageCount/numOfPagesWithWord);
        return idf;
    }
}

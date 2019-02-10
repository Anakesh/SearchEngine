package indexer;

import databaseComunication.DatabaseIndexerCom;
import databaseComunication.entity.WebPage;
import databaseComunication.entity.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Pavel on 07.02.2019.
 */
public class IndexStarter implements Runnable {
    private DatabaseIndexerCom databaseIndexerCom;
    private ExecutorService executor;

    public IndexStarter(DatabaseIndexerCom databaseIndexerCom, int numOfThreads) {
        this.databaseIndexerCom = databaseIndexerCom;
        this.executor = Executors.newFixedThreadPool(numOfThreads);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                List<WebPage> webPagesToIndex = databaseIndexerCom.getPagesToIndex(10);
                if(webPagesToIndex==null){
                    Thread.sleep(10000);
                } else{
                    List<Callable<Boolean>> callables = new ArrayList<>();
                    for(WebPage webPage: webPagesToIndex){
                        callables.add(new Indexer(webPage, databaseIndexerCom));
                    }
                    List<Future<Boolean>> futureTask = executor.invokeAll(callables);
                    System.out.println("\n\n\nfinished tasks");
                    updateAllTfIdf();
                    System.out.println("updated all weights");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    executor.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        }
    }



    private void updateAllTfIdf() throws InterruptedException {
        List<Word> wordList = databaseIndexerCom.getAllWords();
        List<Callable<Boolean>> callables = new ArrayList<>();
        for(Word word:wordList){
            callables.add(new WordTfIdfUpdate(databaseIndexerCom,word));
        }
        List<Future<Boolean>> results = executor.invokeAll(callables);
    }
}

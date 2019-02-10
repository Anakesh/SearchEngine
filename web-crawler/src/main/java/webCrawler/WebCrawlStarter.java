package webCrawler;

import databaseComunication.DatabaseWebCrawlerCom;
import databaseComunication.entity.WebPage;
import databaseComunication.entity.WebsiteToCrawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class WebCrawlStarter implements Runnable {
    private BlockingQueue<WebPage> webPageOutQueue = new LinkedBlockingQueue<>();
    private DatabaseWebCrawlerCom databaseWebCrawlerCom;
    private Thread webPageAdderThread;
    private ExecutorService executor;


    public WebCrawlStarter(DatabaseWebCrawlerCom databaseWebCrawlerCom, int numOfThreads) {
        this.databaseWebCrawlerCom = databaseWebCrawlerCom;
        this.webPageAdderThread = new Thread(new WebPageAdder(databaseWebCrawlerCom,webPageOutQueue));
        this.executor = Executors.newFixedThreadPool(numOfThreads);
    }



    @Override
    public void run() {
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser();
        webPageAdderThread.start();
        while(!Thread.currentThread().isInterrupted()){
            try {
                WebsiteToCrawl websiteToCrawl = databaseWebCrawlerCom.takeWebsiteToCrawl();
                if(websiteToCrawl==null){
                    Thread.sleep(10000);
                } else{
                    String url = websiteToCrawl.getUrl();
                    List<String> disallowedDirs = robotsTxtParser.parseRobotsTxt(url);
                    executor.execute(new WebCrawler(webPageOutQueue, url,disallowedDirs));
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                try {
                    executor.awaitTermination(0, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                webPageAdderThread.interrupt();
                Thread.currentThread().interrupt();
            }
        }
    }
}


package webCrawler;

import databaseComunication.DatabaseWebCrawlerCom;
import databaseComunication.entity.WebPage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Pavel on 06.02.2019.
 */
public class WebPageAdder implements Runnable {
    private DatabaseWebCrawlerCom databaseWebCrawlerCom;
    private BlockingQueue<WebPage> webPageInQueue;

    public WebPageAdder(DatabaseWebCrawlerCom databaseWebCrawlerCom, BlockingQueue<WebPage> webPageInQueue) {
        this.databaseWebCrawlerCom = databaseWebCrawlerCom;
        this.webPageInQueue = webPageInQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                WebPage webPage = webPageInQueue.take();
                if (databaseWebCrawlerCom.webPageExists(webPage)) {
                    WebPage databaseWebPage = databaseWebCrawlerCom.getWebPageByUrl(webPage.getUrl());
                    if (databaseWebPage.getIndexed()&&(!databaseWebPage.getCrc().equals(webPage.getCrc())||databaseWebPage.getIndexDate().isBefore(LocalDateTime.now().minus(7, ChronoUnit.DAYS))))
                        databaseWebCrawlerCom.updateWebPage(webPage);
                } else {
                    databaseWebCrawlerCom.addWebPage(webPage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}

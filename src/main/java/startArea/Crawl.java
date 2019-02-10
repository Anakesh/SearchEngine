package startArea;

import databaseComunication.DatabaseConnect;
import databaseComunication.DatabaseWebCrawlerCom;
import databaseComunication.entity.WebsiteToCrawl;
import webCrawler.WebCrawlStarter;

import java.util.Scanner;

public class Crawl {
    public static void main(String[] args) {
        try {
            DatabaseConnect databaseConnect = new DatabaseConnect();
            DatabaseWebCrawlerCom databaseWebCrawlerCom = new DatabaseWebCrawlerCom(databaseConnect);

            WebsiteToCrawl websiteToCrawl = new WebsiteToCrawl();
            websiteToCrawl.setUrl("https://habr.com/");
            databaseWebCrawlerCom.addWebsiteToCrawl(websiteToCrawl);

            WebCrawlStarter webCrawlStarter = new WebCrawlStarter(databaseWebCrawlerCom, 1);
            Thread thread = new Thread(webCrawlStarter);
            thread.start();
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            while(thread.isAlive()) {
                thread.interrupt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

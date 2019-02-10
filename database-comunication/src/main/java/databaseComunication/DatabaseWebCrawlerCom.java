package databaseComunication;

import databaseComunication.entity.WebPage;
import databaseComunication.entity.WebsiteToCrawl;
import org.hibernate.SessionFactory;
import databaseComunication.repository.WebPageRep;
import databaseComunication.repository.WebsiteToCrawlRep;

/**
 * Created by Pavel on 07.02.2019.
 */
public class DatabaseWebCrawlerCom {
    private WebPageRep webPageRep;
    private WebsiteToCrawlRep websiteToCrawlRep;

    public DatabaseWebCrawlerCom(DatabaseConnect databaseConnect){
        SessionFactory sessionFactory = databaseConnect.getSessionFactory();
        webPageRep = new WebPageRep(sessionFactory);
        websiteToCrawlRep = new WebsiteToCrawlRep(sessionFactory);
    }

    public boolean webPageExists(WebPage webPage) {
        return webPageRep.exists(webPage);
    }

    public WebPage getWebPageByUrl(String url) {
        return webPageRep.getByUrl(url);
    }

    public void updateWebPage(WebPage webPage) {
        webPageRep.update(webPage);
    }

    public void addWebPage(WebPage webPage) {
        webPageRep.add(webPage);
    }

    public WebsiteToCrawl takeWebsiteToCrawl() {
        return websiteToCrawlRep.popFirst();
    }

    public void addWebsiteToCrawl(WebsiteToCrawl websiteToCrawl) {
        websiteToCrawlRep.add(websiteToCrawl);
    }
}

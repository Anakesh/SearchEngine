package webCrawler;

import databaseComunication.entity.WebPage;

import java.util.Set;

/**
 * Created by Pavel on 07.02.2019.
 */
public class ParseResult {
    private WebPage webPage;
    private Set<String> foundUrls;

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public Set<String> getFoundUrls() {
        return foundUrls;
    }

    public void setFoundUrls(Set<String> foundUrls) {
        this.foundUrls = foundUrls;
    }
}

package webCrawler;

import databaseComunication.entity.WebPage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Pavel on 06.02.2019.
 */
public class WebCrawler implements Runnable {

    private BlockingQueue<WebPage> outputQueue;
    private PageParser pageParser;
    private Queue<String> foundPages = new ArrayDeque<>();
    private List<String> parsedPages = new ArrayList<>();

    public WebCrawler(BlockingQueue<WebPage> outputQueue, String inputUrl, List<String> disallowedDirs) {
        this.outputQueue = outputQueue;
        this.pageParser = new PageParser(disallowedDirs);
        foundPages.add(inputUrl);
    }

    @Override
    public void run() {
        while(!foundPages.isEmpty()&&!Thread.currentThread().isInterrupted()){
            try {
                String currUrl = foundPages.poll();
                if(!parsedPages.contains(currUrl)) {
                    ParseResult parseResult =  pageParser.crawlWebPage(currUrl);
                    parsedPages.add(currUrl);
                    if(parseResult!=null){
                        foundPages.addAll(parseResult.getFoundUrls());
                        WebPage webPage = parseResult.getWebPage();
                        if(webPage!=null)
                            outputQueue.add(webPage);
                    }
                    Thread.sleep(2000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }


}

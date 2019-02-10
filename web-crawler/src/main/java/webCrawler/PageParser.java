package webCrawler;

import databaseComunication.entity.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pavel on 07.02.2019.
 */
public class PageParser {

    private static List<String> badTags = Arrays.asList("a", "img", "form", "script", "link", "meta");
    private static List<String> newLineTags =
            Arrays.asList("p", "h", "h1", "h2", "h3", "h4", "h5", "h6");
    private static Pattern baseUrlPattern = Pattern.compile("^((http)|(https))://.+\\.\\w+/");
    private static Pattern urlTrimPattern = Pattern.compile("^((http)|(https))://.+\\.\\w+.*(?=((\\?)|(#)))");
    private List<String> disallowedDirs;

    public PageParser(List<String> disallowedDirs) {
        this.disallowedDirs = disallowedDirs;
    }

    public ParseResult crawlWebPage(String inputUrl) throws IOException, InterruptedException {
        System.out.println("\t\t\t\t\t" + inputUrl);
        Matcher urlMatcher = baseUrlPattern.matcher(inputUrl);
        if (urlMatcher.find()) {
            String baseUrl = urlMatcher.group();
            Pattern urlPattern = Pattern.compile("^" + baseUrl);
            Document doc = Jsoup.connect(inputUrl).get();
            Elements urlElements = doc.getElementsByAttribute("href");
            Set<String> urls = new HashSet<>();
            for (Element element : urlElements) {
                String url = element.absUrl("href");
                Matcher sameSiteMatcher = urlPattern.matcher(url);
                Matcher matcher = urlTrimPattern.matcher(url);
                if (isAllowed(url, disallowedDirs) && sameSiteMatcher.find() && matcher.find()) {
                    urls.add(matcher.group());
                }
            }
            ParseResult parseResult = new ParseResult();
            parseResult.setFoundUrls(urls);
            String title = doc.title();

            Element body = doc.body();
            if (body != null) {
                String text = getText(body);

                WebPage webPage = new WebPage();
                webPage.setCrc(String.valueOf(text.hashCode()));
                webPage.setText(text);
                webPage.setUrl(inputUrl);
                webPage.setTitle(title);
                webPage.setIndexed(false);
                parseResult.setWebPage(webPage);


            }
            return parseResult;
        } else {
            System.out.println("Wrong url format. URL: " + inputUrl);
            return null;
        }
    }

    public static String getText(Element currentElement) {
        StringBuilder output = new StringBuilder();
        String elementTag = currentElement.tagName();
        if (elementTag.equals("br"))
            output.append('\n');
        if (!currentElement.ownText().isEmpty() && isGoodTag(elementTag)) {
            if (isNewLineTag(elementTag))
                output.append('\n').append(currentElement.ownText()).append('\n');
            else
                output.append(currentElement.ownText());
        }
        Elements children = currentElement.children();
        if (!children.isEmpty()) {
            for (Element element : children) {
                if (element != null)
                    output.append(getText(element));
            }
        }
        return output.toString();
    }

    private static boolean isGoodTag(String tag) {
        if (badTags.contains(tag.toLowerCase().trim()))
            return false;
        else
            return true;
    }

    private static boolean isNewLineTag(String tag) {
        if (newLineTags.contains(tag.toLowerCase().trim()))
            return false;
        else
            return true;
    }

    private boolean isAllowed(String url, List<String> disallowedDirs) {
        for (String disallowedDir : disallowedDirs) {
            if (url.contains(disallowedDir))
                return false;
        }
        return true;
    }
}

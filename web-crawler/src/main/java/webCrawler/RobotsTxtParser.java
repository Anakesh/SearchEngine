package webCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pavel on 06.02.2019.
 */
public class RobotsTxtParser {
    private Pattern baseUrlPattern = Pattern.compile("^((http)|(https))://.+\\.\\w+/");

    public List<String> parseRobotsTxt(String url) throws IOException {
        Matcher urlMatcher = baseUrlPattern.matcher(url);
        if(urlMatcher.find()) {
            String baseUrl = urlMatcher.group();
            List<String> output = new ArrayList<>();
            Pattern disallowPattern = Pattern.compile("(?<=(Disallow:)).*");
            String robotsTxt = readRobotsTxt(baseUrl + "robots.txt");
            String[] userAgents = robotsTxt.split("User-agent:");
            for (String userAgent : userAgents) {
                if (userAgent.trim().matches("^\\*[\\w\\W\\s]*")) {
                    String[] lines = userAgent.split("\n");
                    for (String line : lines) {
                        Matcher matcher = disallowPattern.matcher(line);
                        if (matcher.find()) {
                            output.add(matcher.group().trim());
                        }
                    }
                }
            }
            return output;
        } else{
            throw new IOException("Wrong web page format");
        }
    }

    private String readRobotsTxt(String strUrl){
        StringBuilder out = null;
        try {

            URL url = new URL(strUrl);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            out = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line).append('\n');
            }
            in.close();

        }
        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        return out.toString();
    }
}

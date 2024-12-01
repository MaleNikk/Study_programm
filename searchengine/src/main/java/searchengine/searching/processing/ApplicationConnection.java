package searchengine.searching.processing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class ApplicationConnection {

    public static String getDataFromSite(String siteLink){
        try {
            Document document = Jsoup.connect(siteLink).get();
            return document.getAllElements().toString();
        } catch ( IOException exception) {
            log.debug("Site link is bad!");
            return "bad";
        }
    }

    public static String getTextFromSite(String siteLink){
        try {
            Document document = Jsoup.connect(siteLink).get();
            return document.text().toLowerCase().replaceAll("[^a-zа-я]", " ").trim();
        } catch ( IOException exception) {
            log.debug("Not found text on site!");
            return "empty";
        }
    }

    public static String getTitleFromSite(String siteLink){
        try {
            Document document = Jsoup.connect(siteLink).get();
            return document.title();
        } catch ( IOException exception) {
            log.info("Something wrong, please return search!");
            return "Something wrong, please return search!";
        }
    }

    public static String getSnippet(String siteLink, String word) {
        try {
            Document document = Jsoup.connect(siteLink).get();
            String[] snippetParts = document.text().toLowerCase().split(word.toLowerCase());
            List<String> snippets = new ArrayList<>();
            String snippet = "";
            for (int i = 0; i < snippetParts.length; i++){
                if (i > 0 && i <= snippetParts.length-1){
                    String before = snippetParts[i-1];
                    String after = snippetParts[i];
                    if (before.length() > 55){
                        before = before.substring(before.length() - 55, before.length() - 1);
                    }
                    if (after.length() > 55){
                        after = after.substring(0, 55);
                    }
                    snippet = "!snippet! --- "
                            .concat(before)
                            .concat(" <b> ")
                            .concat(word.toUpperCase())
                            .concat(" </b> ")
                            .concat(after)
                            .concat(" ---- !snippet!")
                            .concat("\r\n");
                }
                snippets.add(snippet);
                snippet = "";
            }
            return snippets.toString();
        } catch ( IOException exception) {
            log.debug("This site is not available!");
            return "empty";
        }
    }

    public static boolean checkSite(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return false;
        }
        return uri.isAbsolute();
    }
}

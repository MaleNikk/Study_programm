package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;

@Slf4j
public final class FoundDataSite {

    private FoundDataSite(){}

    public static HashSet<String> getSiteWords(Document document) {
        HashSet<String> words = new HashSet<>();
        Arrays.stream(document.getAllElements().text().split("\\s+"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(word -> word.length() >= 3 && word.length() <= 17)
                .filter(word -> Pattern.compile(FixedValue.REGEX_NO_ABC).matcher(word).find())
                .forEach(words::add);
        return words;
    }

    public static HashSet<String> getSiteLinks(Document document) {
        HashSet<String> sites = new HashSet<>();
        document.getAllElements()
                .stream()
                .filter(element ->
                        element.attr(FixedValue.CHECK_LINK_HTML).contains(FixedValue.CHECK_LINK_HTTP) ||
                                element.attr(FixedValue.CHECK_LINK_HTML).contains(FixedValue.CHECK_LINK_HTTPS))
                .forEach(element -> sites.add(element.attr(FixedValue.CHECK_LINK_HTML)));
        return sites;
    }

    public static Document getDocument(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
        return document;
    }

    public static boolean checkUrl(String url) {
        String checkedType = url.substring(url.length() - 6, url.length() - 1);
        return (url.contains("{") ||
                url.contains("}")) ||
                url.contains("=") ||
                url.contains("?") ||
                checkedType.contains(".css") ||
                checkedType.contains(".csv") ||
                checkedType.contains(".js") ||
                checkedType.contains(".png");
    }
}

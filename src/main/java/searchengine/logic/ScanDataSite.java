package searchengine.logic;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import searchengine.config.FixedValue;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public final class ScanDataSite {

    public String textForDataBase(Document document) {
        StringBuilder builder = new StringBuilder();
        document.getElementsByAttribute("title").forEach(element ->
                addText(element.attr("title"),builder));
        document.getElementsMatchingText("[\\w+\\W+]").forEach(element ->
                addText(element.text(),builder));
        return builder.toString();
    }

    public String textForSnippet(Document document){
        HashSet<String> text = new HashSet<>();
        document.getElementsByAttribute("title").forEach(element ->
                text.add(element.attr("title")));
        document.getElementsMatchingText("[\\w+\\W+]").forEach(element ->
                text.add(element.text()));
        return String.join(". ",text);
    }

    public HashSet<String> sitePages(Document document) {
        String url = document.baseUri();
        HashSet<String> links = new HashSet<>();
        for (Element first : document.getElementsByAttribute("href")) {
            String data = first.attr("href");
            if (!data.startsWith(FixedValue.CHECK_LINK_HTTP) && data.contains("/")) {
                data = url.endsWith("/") ? url.concat(data.replaceFirst("/", "")) : url.concat(data);
            }
            if (data.startsWith(FixedValue.CHECK_LINK_HTTP)) {
                links.add(data);
            }
        }
        return links;
    }

    public Document initDocument(String url) {
        Document document = null;
        try {
            if (url.startsWith(FixedValue.CHECK_LINK_HTTP)) {
                document = Jsoup.connect(url).get();
            }
        } catch (IOException e) {
            return null;
        }
        return document;
    }

    private void addText(String text, StringBuilder builder){
        if (text.trim().length()>1){
            builder.append(text).append(". ");
        }
    }
}

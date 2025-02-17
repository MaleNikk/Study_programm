package searchengine.searching.processing.connect;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.searching.processing.constant.FixedValue;

import java.io.IOException;
import java.util.*;

@Slf4j
public final class FoundDataSite {

    public String getSiteText(Document document) {

        StringBuilder builder = new StringBuilder();

        Elements elements = document.getAllElements();

        for (Element element : elements){

            Attribute attribute = element.attribute("title");

            String text = element.text();

            if (attribute != null){

                builder.append(attribute.getValue()).append(" ");
            }
            if (!text.isBlank()){
                builder.append(text).append(" ");
            }
        }
        return builder.toString();
    }

    public HashSet<String> getSiteLinks(Document document) {

        HashSet<String> links = new HashSet<>();

        for (Element element : document.getAllElements()) {

            Attribute attribute = element.attribute("href");

            if (attribute != null) {

                String link = attribute.getValue();

                if (link.contains("/")) {

                    boolean checkLink = !link.contains(FixedValue.CHECK_LINK_HTTP);

                    if (checkLink) {

                        String baseUrl = document.baseUri();

                        if (baseUrl.endsWith("/")) {

                            baseUrl = baseUrl.substring(0, baseUrl.length() - 2);
                        }

                        link = baseUrl.concat(link);
                    }

                    if (link.substring(0, 5).contains(FixedValue.CHECK_LINK_HTTP)) {
                        links.add(link);
                    }
                }
            }
        }
        return links;
    }

    public Document getDocument(String url) {

        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
        return document;
    }
}

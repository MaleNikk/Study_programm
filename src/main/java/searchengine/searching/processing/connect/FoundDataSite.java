package searchengine.searching.processing.connect;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import searchengine.searching.processing.constant.FixedValue;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
public final class FoundDataSite {

    public HashSet<String> getSiteWords(Document document) {
        HashSet<String> words = new HashSet<>();
        Scanner scanner = new Scanner(document
                .getAllElements().text().toLowerCase().replaceAll(FixedValue.REGEX_ABC," "));
        while (scanner.hasNext()){
            String word =  scanner.next().strip();
            if (word.length() > 2 && word.length() < 19 && !word.isBlank()) {
                words.add(word);
            }
        }
        scanner.close();
        return words;
    }

    public HashMap<Integer, HashSet<String>> getSiteLinks(String url) {
        int keyValid = 1;
        int keyInvalid = 0;
        HashMap<Integer, HashSet<String>> links = new HashMap<>(Map.of(
                keyValid, new HashSet<>(),
                keyInvalid, new HashSet<>()));
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String data = scanner.next();
                if (data.contains(FixedValue.CHECK_LINK_HTTP)|| data.contains(FixedValue.CHECK_LINK_HTTPS)) {
                    String path = data.split("\"")[1];
                    if (path.length() > 4 && path.substring(0,4).contains("http")) {
                        links.get(keyValid).add(path);
                    }
                }
            }
            scanner.close();
        } catch (IOException exception) {
            links.get(keyInvalid).add(url);
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

package searchengine.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ResultAnswer;
import searchengine.config.FixedValue;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Slf4j
public class ThreadFinderData extends Thread {

    private final TreeMap<Double, ConcurrentLinkedQueue<ResultAnswer>> results;

    private final ConcurrentLinkedQueue<List<ModelWord>> listModels;

    private final List<String> lemmas;

    private final ScanDataSite dataSite;

    @Override
    public void run() {
        log.info("Start find result search!");
        while (!listModels.isEmpty()) {
            List<ModelWord> models = listModels.poll();
            buildAnswer(models);
        }
        log.info("Thread for build answer interrupt!");
        this.interrupt();
    }

    private void buildAnswer(List<ModelWord> models) {
        ModelWord modelWord = models.get(FixedValue.ZERO);
        Document document = dataSite.initDocument(modelWord.url());
        if (document != null) {
            String text = dataSite.textForSnippet(document);
            String snippet = foundSnippet(text);
            String title = document.title();
            String parentPage = findParentPage(modelWord.url());
            String childPage = findChildPage(modelWord.url(), parentPage);
            double relevance = findRelevance(models);
            if (!snippet.isBlank()) {
                ResultAnswer answer =
                        new ResultAnswer(parentPage, modelWord.name(), childPage, title, snippet, relevance);
                if (results.containsKey(relevance)) {
                    results.get(relevance).add(answer);
                } else {
                    results.put(relevance, new ConcurrentLinkedQueue<>(Set.of(answer)));
                }
            }
        }
    }

    private double findRelevance(List<ModelWord> models) {
        int defaultSize = Objects.equals(models.size(), lemmas.size()) ? 500 : 1000;
        for (ModelWord model : models) {
            defaultSize = defaultSize - model.frequency();
        }
        return defaultSize / 1000.0;
    }

    private String findChildPage(String url, String parentPage) {
        String childPage = url.replace(parentPage, "");
        return childPage.startsWith("/") ? childPage : "/";
    }

    private String findParentPage(String url) {
        String regexParentUrl = "(?iu)(^http.?://)([\\w.-])*[^/]?";
        Matcher matcher = Pattern.compile(regexParentUrl).matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return url;
    }

    public String foundSnippet(String text) {
        StringBuilder builder = new StringBuilder("</br>...");
        String[] data = text.split("[.,]");
        for (String snippet : data) {
            if (snippet.length() < 250 && snippet.length() > 50) {
                for (String find : lemmas) {
                    Matcher matcher = Pattern.compile("(?iu)\\s".concat(find)
                            .concat("[a-zA-Zа-яА-ЯёЁ]*")).matcher(snippet);
                    if (matcher.find()) {
                        String word = matcher.group();
                        String textData = snippet.replaceAll(word,"<b>".concat(word.toUpperCase()).concat("</b> "));
                        return builder.append(textData).append("...").toString();
                    }
                }
            }
        }
        return "";
    }
}
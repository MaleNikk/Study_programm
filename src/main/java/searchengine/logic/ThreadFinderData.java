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
            String title = document.title();
            String parentPage = findParentPage(modelWord.url());
            String childPage = findChildPage(modelWord.url(), parentPage);
            String snippet = foundSnippet(text);
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
        int defaultSize = 1000 - models.size();
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
        String snippet = "Snippet not found!";
        String[] strings = text.split("\\.");
        for (String string : strings) {
            List<Boolean> check = new ArrayList<>();
            for (String find : lemmas) {
                String regexWord = "(?iu)".concat(find).concat("([a-zA-Zа-яА-ЯёЁ]*)?");
                Matcher matcher = Pattern.compile(regexWord).matcher(string);
                if (matcher.find()) {
                    check.add(true);
                }
            }
            if (check.size() == lemmas.size()) {
                break;
            }
        }
        for (String lemma1 : lemmas) {
            String regexWord = "(?iu)".concat(lemma1).concat("([a-zA-Zа-яА-ЯёЁ]*)?");
            Matcher matcher = Pattern.compile(regexWord).matcher(snippet);
            if (matcher.find()) {
                String word = matcher.group();
                snippet = "</br>...".concat(matcher.replaceAll("<b>".concat(word.toUpperCase()).concat("</b>")))
                        .concat("....");
            }
        }
        return snippet;
    }
}


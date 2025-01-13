package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.SearchResultAnswer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public record DataSearchEngine(TreeMap<Integer, LinkedHashSet<SearchResultAnswer>> results,
                               List<Boolean> resultLoadData,
                               List<ModelWord> words,
                               ModelSearch modelSearch, Integer indexSearch,
                               String name) implements Runnable {

    @Override
    public void run() {
        log.info("Init tread for search: {}", Thread.currentThread().getName());
        findByWord();
    }

    public void findByWord() {
        AtomicInteger indexThread = new AtomicInteger(FixedValue.ZERO);
        if (!words().isEmpty()) {
            for (ModelWord modelWord : words()) {
                if (Objects.equals(indexThread.get(), indexSearch()) && modelWord != null) {
                    if (Objects.equals(modelSearch().getParentSite(), FixedValue.SEARCH_IN_ALL)) {
                        modelWord.sites()
                                .values()
                                .forEach(sites -> sites
                                        .forEach(site -> buildDataResult(site, modelWord)));
                    } else {
                        modelWord.sites().get(modelSearch().getParentSite().hashCode())
                                .forEach(site -> buildDataResult(site, modelWord));
                    }
                }
                if (Objects.equals(indexThread.get(), FixedValue.COUNT_THREADS)) {
                    indexThread.set(0);
                }
                indexThread.getAndIncrement();
            }
        }
        resultLoadData().add(FixedValue.TRUE);
        log.info("Searching complete!");
        Thread.currentThread().interrupt();
    }

    private void buildDataResult(String site, ModelWord modelWord) {
        Document document = FoundDataSite.getDocument(site);
        if (document != null) {
            String text = document.getAllElements().text();
            String page = getPageUri(site, modelSearch().getParentSite());
            String parentSite = page.equalsIgnoreCase("/") ? site : site.split(page, 2)[0];
            String snippet = getSnippets(text, modelWord.word());
            Double relevance = getRelevance(modelSearch().getWord(), modelWord.word());
            Integer keyMap = relevance <= 1.0 ? (10 - (int) (relevance * 10)) : 11;
            if (!snippet.isBlank()) {
                results().get(keyMap).add(
                        new SearchResultAnswer(parentSite, name(), page, document.title(), snippet, relevance));
            }
        }
    }

    private Double getRelevance(String searchWord, String savedWord) {
        char[] charsSearch = searchWord.toCharArray();
        char[] charsSaved = savedWord.toCharArray();
        double relevance;
        int indexStart;
        int indexLoop = Math.min(charsSearch.length, charsSaved.length);
        if (charsSearch.length <= 5) {
            indexStart = 2;
            relevance = charsSaved.length > charsSearch.length ? 0.2 : 0.3;
        } else {
            indexStart = 4;
            relevance = charsSaved.length > charsSearch.length ? 0.4 : 0.5;
        }
        for (int i = indexStart; i < indexLoop; i++) {
            if (Objects.equals(charsSearch[i], charsSaved[i])) {
                relevance = relevance + 0.1;
            }
        }
        return relevance;
    }

    private String getSnippets(String text, String word) {
        HashSet<String> snippets = new HashSet<>();
        String[] data = text.split("\\s+");
        StringBuilder snippet;
        for (int i = 0; i < data.length; i++) {
            if (data[i].equalsIgnoreCase(word)) {
                snippet = new StringBuilder();
                snippet.append("<br>.....");
                for (int b = i - 6; b < i; b++) {
                    if (b >= 0) {
                        snippet.append(" ").append(data[b]);
                    }
                }
                snippet.append(" <b>").append(data[i].toUpperCase()).append("</b>");
                for (int c = i + 1; c < i + 6 && c < data.length; c++) {
                    snippet.append(" ").append(data[c]);
                }
                snippet.append(".......");
                snippets.add(snippet.toString());
            }
        }
        return snippets.toString();
    }

    private String getPageUri(String site, String parentSite) {
        String[] data = site.split("/", 4);
        return site.equalsIgnoreCase(parentSite) ? "/" :
                Objects.equals(data.length, 4) ? site.split("/", 4)[3] : "/";
    }
}

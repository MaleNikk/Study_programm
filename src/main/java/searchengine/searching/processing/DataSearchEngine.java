package searchengine.searching.processing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.SearchResultAnswer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Slf4j
public class DataSearchEngine implements Runnable {

    private final TreeMap<Integer, LinkedHashSet<SearchResultAnswer>> results;

    private final List<Boolean> resultLoadData;

    private final List<ModelWord> words;

    private final ModelSearch modelSearch;

    private final Integer indexSearch;

    @Override
    public void run() {
        log.info("Init tread for search: {}", Thread.currentThread().getName());
        findByWord();
    }

    public void findByWord() {
        AtomicInteger indexThread = new AtomicInteger(FixedValue.ZERO);
        if (!words.isEmpty()) {
            for (ModelWord modelWord : words) {
                if (Objects.equals(indexThread.get(), indexSearch) && modelWord != null) {
                    if (Objects.equals(modelSearch.getParentSite(), FixedValue.SEARCH_IN_ALL)) {
                        buildDataResult(modelSearch.getWord(), modelWord);
                    } else {
                        if (modelWord.parentUrl().equalsIgnoreCase(modelSearch.getParentSite())) {
                            buildDataResult(modelSearch.getWord(), modelWord);
                        }
                    }
                }
                if (Objects.equals(indexThread.get(), FixedValue.COUNT_THREADS)) {
                    indexThread.set(0);
                }
                indexThread.getAndIncrement();
            }
        }
        resultLoadData.add(FixedValue.TRUE);
        log.info("{} for searching interrupt!", Thread.currentThread().getName());
        Thread.currentThread().interrupt();
    }

    private void buildDataResult(String searchingWord, ModelWord modelWord) {
        FoundDataSite foundDataSite = new FoundDataSite();
        Document document = foundDataSite.getDocument(modelWord.url());
        if (document != null) {
            String text = document.getAllElements().text();
            String page = getPageUri(modelWord.url(), modelSearch.getParentSite());
            String parentSite = page.equalsIgnoreCase("/") ?
                    modelWord.url() : page.isBlank() ?
                    modelWord.url().substring(FixedValue.ZERO,modelWord.url().length()-1) :
                    modelWord.url().split(page, 2)[0];
            page = page.isBlank() ? "/" : page;
            String snippet = getSnippets(text, searchingWord);
            Double relevance = getRelevance(searchingWord, modelWord.word());
            Integer keyMap = relevance <= 1.0 ? (10 - (int) (relevance * 10)) : 11;
            if (!snippet.isBlank()) {
                results.get(keyMap).add(
                        new SearchResultAnswer(parentSite,modelWord.name(),page,document.title(),snippet,relevance));
            }
        }
    }

    private Double getRelevance(String searchWord, String savedWord) {
        char[] charsSearch = searchWord.toCharArray();
        char[] charsSaved = savedWord.toCharArray();
        double relevance;
        int indexStart;
        int indexLoop = Math.min(charsSearch.length, charsSaved.length);
        if (charsSearch.length > 7) {
            indexStart = 6;
            relevance = charsSaved.length > charsSearch.length ? 0.4 : 0.5;

        } else if (charsSearch.length < 4) {
            indexStart = 0;
            relevance = charsSaved.length > charsSearch.length ? 0.0 : 0.1;
        } else {
            indexStart = 3;
            relevance = charsSaved.length > charsSearch.length ? 0.3 : 0.4;
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
        return site.equalsIgnoreCase(parentSite) || !Objects.equals(data.length, 4) ? "/" :
                (data[3].isBlank() ? "" : data[3]);
    }
}


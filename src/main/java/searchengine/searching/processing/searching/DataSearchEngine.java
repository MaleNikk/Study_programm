package searchengine.searching.processing.searching;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.SearchResultAnswer;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.processing.connect.FoundDataSite;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Slf4j
public class DataSearchEngine implements Runnable {

    private final TreeMap<Integer, ConcurrentLinkedQueue<SearchResultAnswer>> results;

    private final AtomicInteger resultWork;

    private final ConcurrentLinkedQueue<ModelWord> words;

    private final ModelSearch modelSearch;

    @Override
    public void run() {
        log.info("Init tread for search: {}", Thread.currentThread().getName());
        findByWord();
    }

    public void findByWord() {
        if (!words.isEmpty()) {
            do {
                ModelWord modelWord = words.poll();
                if (Objects.equals(modelSearch.getParentSite(), FixedValue.SEARCH_IN_ALL)) {
                    buildDataResult(modelSearch.getWord(), modelWord);
                } else {
                    if (modelWord.parentUrl().equalsIgnoreCase(modelSearch.getParentSite())) {
                        buildDataResult(modelSearch.getWord(), modelWord);
                    }
                }
            } while (!words.isEmpty());
        }
        resultWork.getAndIncrement();
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
                    modelWord.url().substring(FixedValue.ZERO, modelWord.url().length() - 1) :
                    modelWord.url().split(page, 2)[0];
            page = page.isBlank() ? "/" : page;
            String snippet = getSnippets(text, modelWord.word());
            Double relevance = getRelevance(searchingWord, modelWord.word());
            Integer keyMap = relevance <= 1.0 ? (10 - (int) (relevance * 10)) : 11;
            if (!snippet.isBlank()) {
                results.get(keyMap).add(
                        new SearchResultAnswer(parentSite, modelWord.name(), page, document.title(), snippet, relevance));
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
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(text).useDelimiter("\\s+");
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        StringBuilder snippet;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).toLowerCase().contains(word.toLowerCase())) {
                snippet = new StringBuilder();
                snippet.append("<br>.....");
                for (int b = i - 6; b < i; b++) {
                    if (b >= 0) {
                        snippet.append(" ").append(words.get(b));
                    }
                }
                snippet.append(" <b>").append(words.get(i).toUpperCase()).append("</b>");
                for (int c = i + 1; c < i + 6; c++) {
                    if (c < words.size()) {
                        snippet.append(" ").append(words.get(c));
                    }
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


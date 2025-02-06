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

    private final TreeMap<Double, ConcurrentLinkedQueue<SearchResultAnswer>> results;

    private final AtomicInteger resultWork;

    private final ConcurrentLinkedQueue<ModelWord> words;

    private final ModelSearch modelSearch;

    private final AtomicInteger countWords = new AtomicInteger(1);

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
        log.info("{}: searching complete!", Thread.currentThread().getName());
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
            double relevance = getRelevance(searchingWord, modelWord.word());
            if (!snippet.isBlank()) {
                SearchResultAnswer answer =
                        new SearchResultAnswer(parentSite, modelWord.name(), page, document.title(), snippet, relevance);
                if (results.containsKey(relevance)) {
                    results.get(relevance).add(answer);
                } else {
                    results.put(relevance, new ConcurrentLinkedQueue<>(Set.of(answer)));
                }
            }
        }
    }

    private double getRelevance(String searchWord, String savedWord) {
        char[] charsSearch = searchWord.toCharArray();
        char[] charsSaved = savedWord.toCharArray();
        int minLength = Math.min(charsSearch.length, charsSaved.length);
        int maxLength = Math.max(charsSearch.length, charsSaved.length);
        double relevance = maxLength - minLength > 1 ? 3.0 : 1.5;
        for (int i = 0; i < minLength; i++) {
            if (Objects.equals(charsSearch[i], charsSaved[i])) {
                relevance -= 0.2;
            }
        }
        return relevance - (double)countWords.get()/100;
    }

    private String getSnippets(String text, String word) {
        HashSet<String> snippets = new HashSet<>();
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(text).useDelimiter("\\s+");
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()){
            String find = scanner.next();
            if (!result.isEmpty()) {
                words.add(find);
                if (words.size() == 7 || !scanner.hasNext()) {
                    result.append(" ").append(String.join(" ", words)).append(".....<br>");
                    snippets.add(result.toString());
                    words.clear();
                    result = new StringBuilder();
                }
            } else if (find.toLowerCase().contains(word.toLowerCase())){
                int beginIndex = words.size() >= 7 ? words.size()-7 : 0;
                result.append("<br>.....").append(String.join(" ", words.subList(beginIndex, words.size())))
                        .append(" <b>").append(find.toUpperCase()).append("</b>");
                words.clear();
            } else  {
                words.add(find);
            }
        }
        countWords.set(snippets.size());
        return snippets.toString();
    }

    private String getPageUri(String site, String parentSite) {
        String[] data = site.split("/", 4);
        return site.equalsIgnoreCase(parentSite) || !Objects.equals(data.length, 4) ? "/" :
                (data[3].isBlank() ? "" : data[3]);
    }
}


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
public class DataSearchEngine extends Thread {

    private final TreeMap<Double, ConcurrentLinkedQueue<SearchResultAnswer>> results;

    private final AtomicInteger resultWork;

    private final ConcurrentLinkedQueue<ModelWord> words;

    private final ModelSearch modelSearch;

    private final HashSet<String> findLemmas;

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

        AtomicInteger countWords = new AtomicInteger(1);

        FoundDataSite foundDataSite = new FoundDataSite();

        CalculateRelevance calculateRelevance = new CalculateRelevance();

        SnippetBuilder snippetBuilder = new SnippetBuilder(countWords,findLemmas,calculateRelevance);

        Document document = foundDataSite.getDocument(modelWord.url());

        if (document != null) {

            String text = foundDataSite.getSiteText(document);

            String page = foundPageUri(modelWord.url(), modelSearch.getParentSite());

            String parentSite = foundParentUri(page,modelWord);

            page = page.isBlank() ? "/" : page;

            String snippet = snippetBuilder.foundSnippets(text, modelWord.lemma());

            double relevance = calculateRelevance.foundRelevance(searchingWord, modelWord.word(),countWords.get());

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

    private String foundPageUri(String site, String parentSite) {

        String[] data = site.split("/", 4);

        return site.equalsIgnoreCase(parentSite) || !Objects.equals(data.length, 4) ? "/" :
                (data[3].isBlank() ? "" : data[3]);
    }

    private String foundParentUri(String pageUri, ModelWord modelWord){
        return pageUri.equalsIgnoreCase("/") ?
                modelWord.url() : pageUri.isBlank() ?
                modelWord.url().substring(FixedValue.ZERO, modelWord.url().length() - 1) :
                modelWord.url().split(pageUri, 2)[0];

    }
}


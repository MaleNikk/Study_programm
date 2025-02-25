package searchengine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.AppProperties;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelFinder;
import searchengine.dto.model.ResultAnswer;
import searchengine.dto.model.TotalSearchResult;
import searchengine.config.FixedValue;
import searchengine.logic.ScanDataSite;
import searchengine.logic.ThreadFinderData;
import searchengine.logic.LemmaCreator;
import searchengine.repository.RepositoryProject;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@Slf4j
public class ManagementFindingData implements ServiceFindingData {

    private final RepositoryProject repository;

    private final TreeMap<Double, ConcurrentLinkedQueue<ResultAnswer>> results;

    private final ConcurrentLinkedQueue<List<ModelWord>> listModels;

    private final LemmaCreator morphology;

    private final List<Thread> threads;

    private final AppProperties properties;

    private String lastSearchData;

    @Autowired
    public ManagementFindingData(RepositoryProject repository, LemmaCreator morphology, AppProperties properties) {
        this.morphology = morphology;
        this.properties = properties;
        this.listModels = new ConcurrentLinkedQueue<>();
        this.repository = repository;
        this.results = new TreeMap<>();
        this.threads = new ArrayList<>();
        this.lastSearchData = "";
    }

    @Override
    public TotalSearchResult findByWord(ModelFinder modelFinder) {
        log.info("Init method searching word: {}, {}", modelFinder.getWord(), this.getClass().getName());
        if (!Objects.equals(lastSearchData, modelFinder.getWord())) {
            lastSearchData = modelFinder.getWord();
            listModels.clear();
            results.clear();
            List<String> findLemmas = Stream.of(modelFinder.getWord().trim().split("\\s+"))
                    .map(morphology::getForm).filter(lemma -> lemma.length() > 3).toList();
            startFindWords(findLemmas, modelFinder.getParentSite(), modelFinder.getLimit());
            startThreads(findLemmas);
        }
        return buildTotalAnswer(modelFinder);
    }

    private void startFindWords(List<String> findLemmas, String url, int limit) {
        HashMap<String, List<ModelWord>> dataFromDb = new HashMap<>();
        for (String lemma : findLemmas) {
            repository.takeModelWords(lemma, url, limit).forEach(model -> {
                if (dataFromDb.containsKey(model.url())) {
                    dataFromDb.get(model.url()).add(model);
                } else {
                    dataFromDb.put(model.url(), new ArrayList<>(List.of(model)));
                }
            });
        }
        listModels.addAll(dataFromDb.values().stream().filter(list->list.size()==findLemmas.size()).toList());
    }

    private void startThreads(List<String> findLemmas) {
        threads.clear();
        while (!listModels.isEmpty() && threads.size() < properties.getActiveThreads()) {
            Thread thread = new ThreadFinderData(results, listModels, findLemmas, new ScanDataSite());
            threads.add(thread);
            thread.start();
        }
    }

    private TotalSearchResult buildTotalAnswer(ModelFinder modelFinder) {
        waitBuilder();
        TotalSearchResult searchResult = new TotalSearchResult();
        searchResult.setResult(FixedValue.TRUE);
        searchResult.setError(FixedValue.NO_ERROR);
        searchResult.setCount(results.size());
        searchResult.setData(answers());
        return new TotalSearchResult(FixedValue.TRUE, answers().size(), FixedValue.NO_ERROR,
                answers().subList(FixedValue.ZERO, answers().size() > modelFinder.getLimit() ?
                        modelFinder.getLimit() : answers().size()));
    }

    private List<ResultAnswer> answers() {
        return results.values().stream().flatMap(Collection::stream).toList();
    }

    private void waitBuilder() {
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

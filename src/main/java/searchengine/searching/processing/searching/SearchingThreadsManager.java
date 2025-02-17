package searchengine.searching.processing.searching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.SearchResultAnswer;
import searchengine.dto.model.TotalSearchResult;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public final class SearchingThreadsManager implements SearchingManager {

    private final AppManagementRepositoryImpl managementRepository;

    private final List<Thread> runnableList;

    private String lastSearchWord;

    private final TotalSearchResult totalSearchResult;

    private final TreeMap<Double, ConcurrentLinkedQueue<SearchResultAnswer>> results;

    private final AtomicInteger resultWork;

    private final ConcurrentLinkedQueue<ModelWord> words;

    @Autowired
    public SearchingThreadsManager(AppManagementRepositoryImpl managementRepository) {
        this.words = new ConcurrentLinkedQueue<>();
        this.managementRepository = managementRepository;
        this.runnableList = new ArrayList<>();
        this.results = new TreeMap<>();
        this.resultWork = new AtomicInteger(FixedValue.ZERO);
        this.totalSearchResult = new TotalSearchResult();
        this.lastSearchWord = "";
    }

    @Override
    public TotalSearchResult findByWord(ModelSearch modelSearch) {

        log.info("Init method searching word: {}. {}", modelSearch.getWord(), this.getClass().getName());

        words.clear();

        if (Objects.equals(modelSearch.getParentSite(),null)) {

            modelSearch.setParentSite(FixedValue.SEARCH_IN_ALL);
        }

        List<String> searchingWords = List.of(modelSearch.getWord().trim().split("\\s+"));

        HashSet<String> findLemmas = new HashSet<>();

        buildDataSearching(findLemmas,searchingWords);

        int countThreads = words.size() > FixedValue.COUNT_THREADS ? FixedValue.COUNT_THREADS : words.size();

        if (!Objects.equals(lastSearchWord, modelSearch.getWord())) {

            lastSearchWord = modelSearch.getWord();

            initResultStorage();

            initMultithreadingSearch(modelSearch, countThreads, findLemmas);

            initTimerSearch(countThreads);

            buildSearchResult(modelSearch);
        }

        log.info("Build answer complete!");

        return totalSearchResult;
    }

    private void initMultithreadingSearch(ModelSearch modelSearch, int countThreads, HashSet<String> findLemmas) {

        do {
            runnableList.add(new DataSearchEngine(results, resultWork, words, modelSearch, findLemmas));

        } while (runnableList.size() < countThreads && runnableList.size() < words.size());

        runnableList.forEach(Thread::start);
    }

    private void initResultStorage() {

        runnableList.clear();

        results.clear();

        resultWork.set(FixedValue.ZERO);
    }

    private void buildDataSearching(HashSet<String> findLemmas,  List<String> searchingWords){

        HashMap<String, ModelWord> findUrls = new HashMap<>();

        for (String word : searchingWords) {

            List<ModelWord> modelWords = managementRepository.findModelWords(word);

            if (!modelWords.isEmpty()) {

                for (ModelWord modelWord : modelWords){

                    if (!findUrls.containsKey(modelWord.url())){

                        findUrls.put(modelWord.url(),modelWord);

                        findLemmas.add(modelWord.lemma());
                    }
                }
            }
        }
        words.addAll(findUrls.values());
    }

    private void initTimerSearch(int countThreads) {

        long timeStart = System.nanoTime();

        do {
            log.info("Searching in progress, timeout: {} sec.",

                    TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - timeStart));

            try {

                TimeUnit.MILLISECONDS.sleep(FixedValue.TIME_SLEEP);

            } catch (InterruptedException e) {

                log.info("Current search was interrupted! Please read log file!");

                throw new RuntimeException(e);
            }

        } while (resultWork.get() < countThreads);
    }

    private void buildSearchResult(ModelSearch modelSearch){

        totalSearchResult.setResult(FixedValue.TRUE);

        totalSearchResult.setError(FixedValue.ERROR);

        totalSearchResult.setData(new ArrayList<>());

        results.values().forEach(sites -> totalSearchResult.getData()
                .addAll(sites));

        totalSearchResult.setCount(totalSearchResult.getData().size());

        totalSearchResult.setData(totalSearchResult.getData().subList(FixedValue.ZERO,
                totalSearchResult.getData().size() > modelSearch.getLimit() ? modelSearch.getLimit() :
                        totalSearchResult.getData().size()));
    }
}

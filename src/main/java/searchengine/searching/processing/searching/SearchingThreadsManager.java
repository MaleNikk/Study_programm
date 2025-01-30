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

    private final TreeMap<Integer, ConcurrentLinkedQueue<SearchResultAnswer>> results;

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
        words.addAll(managementRepository.findModelWords(modelSearch.getWord()));
        int countThreads = words.size() > FixedValue.COUNT_THREADS ? FixedValue.COUNT_THREADS : words.size();
        if (!Objects.equals(lastSearchWord, modelSearch.getWord())) {
            lastSearchWord = modelSearch.getWord();
            initResultStorage();
            initMultithreadingSearch(modelSearch, countThreads);
            initTimerSearch(countThreads);
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
        log.info("Searching complete!");
        return totalSearchResult;
    }

    private void initMultithreadingSearch(ModelSearch modelSearch, int countThreads) {
        for (ModelWord modelWord : words ){
            System.out.println(modelWord);
        }
        do {
            Thread searching = new Thread(new DataSearchEngine(results, resultWork, words, modelSearch));
            runnableList.add(searching);
        } while (runnableList.size() < countThreads && runnableList.size() < words.size());
        runnableList.forEach(Thread::start);
    }

    private void initResultStorage() {
        runnableList.clear();
        results.clear();
        resultWork.set(FixedValue.ZERO);
        for (int i = 0; i < 12; i++) {
            results.put(i, new ConcurrentLinkedQueue<>());
        }
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
}

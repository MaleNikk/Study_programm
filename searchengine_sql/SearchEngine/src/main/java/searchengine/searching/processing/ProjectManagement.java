package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.config.SitesList;
import searchengine.dto.entity.*;
import searchengine.dto.model.TotalSearchResult;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.SearchResultAnswer;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public final class ProjectManagement implements AppManagementImpl {

    private final AppManagementRepositoryImpl managementRepository;

    private final SitesList sitesList;

    private final TotalSearchResult totalSearchResult;

    private final TreeMap<Integer, LinkedHashSet<SearchResultAnswer>> results;

    private final List<Boolean> resultLoadData;

    public static volatile String STATUS = FixedValue.INDEXING_NOT_STARTED;

    private String lastSearchWord;

    private final AtomicInteger countThreadSearch;

    public static final AtomicBoolean START_INDEXING = new AtomicBoolean(FixedValue.FALSE);

    @Autowired
    public ProjectManagement(AppManagementRepositoryImpl managementRepository, SitesList sitesList) {
        this.managementRepository = managementRepository;
        this.sitesList = sitesList;
        this.results = new TreeMap<>();
        this.resultLoadData = new ArrayList<>();
        this.totalSearchResult = new TotalSearchResult();
        this.countThreadSearch = new AtomicInteger();
        this.lastSearchWord = "";
    }

    @Override
    public void startIndexing() {
        log.info("Init method startIndexing. {}", this.getClass().getName());
        START_INDEXING.set(FixedValue.TRUE);
        STATUS = FixedValue.IN_PROGRESS;
        initParentSites();
        initIndexing();
    }

    @Override
    public void stopIndexing() {
        log.info("Init method stopIndexing. {}", this.getClass().getName());
        START_INDEXING.set(FixedValue.FALSE);
        STATUS = FixedValue.INDEXED_STOP;
    }

    @Override
    public void addSite(String url, String name) {
        log.info("Init method addSite. {}", this.getClass().getName());
        name = url.contains("www.") ?
                url.split("/")[2].replace("www.", "") :
                url.split("/")[2];
        log.info("Name new site: {}", name);
        managementRepository.delete(url);
        managementRepository.saveParentSites(List.of(FixedValue.getNewModelParentSite(url, name)));
        managementRepository.saveFoundSites(List.of(FixedValue.getNewModelSite(url, url, name)));
    }

    @Override
    public List<ModelSite> showAllSites() {
        log.info("Init method showAllSites. {}", this.getClass().getName());
        return managementRepository.showIndexedSites();
    }

    @Override
    public List<ModelWord> showAllWords() {
        log.info("Init method showAllWords. {}", this.getClass().getName());
        return managementRepository.showIndexedWords();
    }

    @Override
    public TotalSearchResult findByWord(ModelSearch modelSearch) {
        log.info("Init method searching word: {}. {}", modelSearch.getWord(), this.getClass().getName());
        List<ModelWord> words = managementRepository.findModelWords(modelSearch.getWord());
        if (words.size() < FixedValue.COUNT_THREADS) {
            countThreadSearch.set(words.size());
        } else {
            countThreadSearch.set(FixedValue.COUNT_THREADS);
        }
        if (!Objects.equals(lastSearchWord, modelSearch.getWord())) {
            lastSearchWord = modelSearch.getWord();
            if (!results.isEmpty()) {
                results.clear();
            }
            for (int i = 0; i < 12; i++) {
                results.put(i, new LinkedHashSet<>());
            }
            initMultithreadingSearch(modelSearch, words);
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
            } while (!Objects.equals(resultLoadData.size(), countThreadSearch.get()));
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
        return totalSearchResult;
    }

    @Override
    public StatisticsResponse getStatistics() {
        log.info("Init method getStatistics. {}", this.getClass().getName());
        return StatisticBuilder.getStatistics(managementRepository.getParentSites());
    }

    private void initIndexing() {
        if (managementRepository.countFoundSites() < FixedValue.COUNT_THREADS) {
            if (Objects.equals(managementRepository.countFoundSites(), FixedValue.ZERO)) {
                initStartedListSites();
            }
            DataIndexingEngine indexingEngine = new DataIndexingEngine(managementRepository);
            indexingEngine.loadData();
            initIndexing();
        } else {
            initMultithreadingIndexing();
        }
    }

    private void initMultithreadingSearch(ModelSearch modelSearch, List<ModelWord> words) {
        AtomicInteger indexThread = new AtomicInteger(FixedValue.ZERO);
        List<Thread> searchingThreads = new ArrayList<>();
        do {
            searchingThreads.add(getNewThreadSearching(
                    modelSearch,
                    words,
                    indexThread.getAndIncrement()));
        } while (searchingThreads.size() < countThreadSearch.get());
        searchingThreads.forEach(Thread::start);
    }

    private void initMultithreadingIndexing() {
        List<Thread> indexingThreads = new ArrayList<>();
        long startTime = System.nanoTime();
        do {
            indexingThreads.add(getNewThreadIndexing());
        } while (indexingThreads.size() < FixedValue.COUNT_THREADS);
        indexingThreads.forEach(Thread::start);
        do {
            log.info("Indexing in progress, timeout: {} sec.",
                    TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime));
            try {
                TimeUnit.MILLISECONDS.sleep(FixedValue.TIME_SLEEP);
            } catch (InterruptedException e) {
                log.info("Timer closed! We caught exception: {}", e.getMessage());
            }
        } while (START_INDEXING.get()
                && !STATUS.equals(FixedValue.INDEXING_COMPLETE));
    }

    private Thread getNewThreadIndexing() {
        return new Thread(new DataIndexingEngine(managementRepository));
    }

    private Thread getNewThreadSearching(ModelSearch modelSearch, List<ModelWord> words, Integer indexTread) {
        return new Thread(new DataSearchEngine(
                results,
                resultLoadData,
                words,
                modelSearch,
                indexTread));
    }

    private void initStartedListSites() {
        List<ModelSite> foundSiteEntities = new ArrayList<>();
        managementRepository.getParentSites().forEach(parent ->
                foundSiteEntities.add(FixedValue.getNewModelSite(parent.url(), parent.url(), parent.name())));
        foundSiteEntities.forEach(site -> managementRepository.delete(site.parentUrl()));
        managementRepository.saveFoundSites(foundSiteEntities);
    }

    private void initParentSites() {
        if (!sitesList.getSites().isEmpty()) {
            List<ModelParentSite> parentSiteEntities = new ArrayList<>();
            List<ModelSite> foundSiteEntities = new ArrayList<>();
            sitesList.getSites().forEach(site -> {
                ModelParentSite parentSite = FixedValue.getNewModelParentSite(site.getUrl(), site.getName());
                ModelSite modelSite = FixedValue.getNewModelSite(site.getUrl(),site.getUrl(),site.getName());
                parentSiteEntities.add(parentSite);
                foundSiteEntities.add(modelSite);
            });
            managementRepository.saveParentSites(parentSiteEntities);
            managementRepository.saveFoundSites(foundSiteEntities);
        }
    }
}

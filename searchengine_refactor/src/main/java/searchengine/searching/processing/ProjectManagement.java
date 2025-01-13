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
public class ProjectManagement implements AppManagementImpl {

    private final AppManagementRepositoryImpl managementRepository;

    private final SitesList sitesList;

    private final StatisticBuilder statisticBuilder;

    private final TotalSearchResult totalSearchResult;

    private final TreeMap<Integer,LinkedHashSet<SearchResultAnswer>> results;

    private final List<Boolean> resultLoadData;

    public static final AtomicBoolean START_INDEXING = new AtomicBoolean(FixedValue.FALSE);

    public static volatile String STATUS = FixedValue.INDEXING_NOT_STARTED;

    private final AtomicInteger countThreadSearch;

    private String lastSearchWord;

    @Autowired
    public ProjectManagement(AppManagementRepositoryImpl managementRepository, SitesList sitesList) {
        this.managementRepository = managementRepository;
        this.sitesList = sitesList;
        this.lastSearchWord = "";
        this.countThreadSearch = new AtomicInteger();
        this.statisticBuilder = new StatisticBuilder();
        this.totalSearchResult = new TotalSearchResult();
        this.results = new TreeMap<>();
        this.resultLoadData = new ArrayList<>();
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
                url.split("/")[2].replace("www.","") :
                url.split("/")[2];
        log.info("Name new site: {}", name);
        managementRepository.delete(url);
        managementRepository.saveParentSites(List.of(FixedValue.getNewParentSiteEntity(url, name)));
        managementRepository.saveFoundSites(List.of(FixedValue.getNewFoundSite(url, url)));
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
        List<ModelWord> words = managementRepository.findModelWords(modelSearch.getWord(), modelSearch.getParentSite());
        if (words.size() < FixedValue.COUNT_THREADS) {
            countThreadSearch.set(words.size());
        } else {
            countThreadSearch.set(FixedValue.COUNT_THREADS);
        }
        if (!Objects.equals(lastSearchWord, modelSearch.getWord())) {
            lastSearchWord = modelSearch.getWord();
            if (!results.isEmpty()) { results.clear(); }
            for (int i = 0; i < 12; i++) { results.put(i, new LinkedHashSet<>()); }
            initMultithreadingSearch(modelSearch, countThreadSearch.get(),words);
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
            } while (!Objects.equals(resultLoadData.size(), FixedValue.COUNT_THREADS));
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
        return statisticBuilder.getStatistics(managementRepository.getParentSites());
    }

    private void initIndexing() {
        if (managementRepository.countFoundSites() < FixedValue.COUNT_THREADS) {
            if (Objects.equals(managementRepository.countFoundSites(), FixedValue.ZERO) ||
                    Objects.equals(STATUS,FixedValue.INDEXING_COMPLETE)) {
                managementRepository.delete(FixedValue.SEARCH_IN_ALL);
                initStartedListSites();
            }
            DataIndexingEngine indexingEngine = new DataIndexingEngine(managementRepository);
            indexingEngine.loadData();
            initIndexing();
        } else {
            initMultithreadingIndexing();
        }
    }

    private void initMultithreadingSearch(ModelSearch modelSearch, int countThreads, List<ModelWord> words) {
        AtomicInteger indexThread = new AtomicInteger(FixedValue.ZERO);
        List<Thread> searchingThreads = new ArrayList<>();
        do {
            searchingThreads.add(getNewThreadSearching(
                    words,
                    modelSearch,
                    indexThread.getAndIncrement(),
                    managementRepository.getName(modelSearch.getParentSite())));
        } while (searchingThreads.size() < countThreads);
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

    private Thread getNewThreadSearching(List<ModelWord> words,ModelSearch modelSearch,Integer indexTread,String name){
        return new Thread(new DataSearchEngine(
                results,
                resultLoadData,
                words,
                modelSearch,
                indexTread,
                name));
    }

    private void initStartedListSites() {
        log.info("Init started list! {}", this.getClass().getName());
        List<FoundSiteEntity> foundSiteEntities = new ArrayList<>();
        managementRepository.getParentSites().forEach(parentSiteEntity -> {
            ModelParentSite parent = parentSiteEntity.getModelParentSite();
            foundSiteEntities.add(FixedValue.getNewFoundSite(parent.getUrl(), parent.getUrl()));
        });
        managementRepository.saveFoundSites(foundSiteEntities);
    }

    private void initParentSites() {
        if (!sitesList.getSites().isEmpty()) {
            List<ParentSiteEntity> parentSiteEntities = new ArrayList<>();
            List<FoundSiteEntity> foundSiteEntities = new ArrayList<>();
            sitesList.getSites().forEach(site -> {
                ParentSiteEntity parentSite = FixedValue.getNewParentSiteEntity(site.getUrl(), site.getName());
                FoundSiteEntity foundSite = FixedValue.getNewFoundSite(site.getUrl(),site.getUrl());
                parentSiteEntities.add(parentSite);
                foundSiteEntities.add(foundSite);
            });
            managementRepository.saveParentSites(parentSiteEntities);
            managementRepository.saveFoundSites(foundSiteEntities);
        }
    }
}

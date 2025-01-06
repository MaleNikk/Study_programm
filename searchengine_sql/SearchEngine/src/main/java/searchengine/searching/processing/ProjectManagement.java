package searchengine.searching.processing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
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
@Data
@Slf4j
public class ProjectManagement implements AppManagementImpl {

    @Autowired
    private AppManagementRepositoryImpl managementRepository;

    @Autowired
    private SitesList sitesList;

    private final StatisticBuilder statisticBuilder = new StatisticBuilder();

    private final FoundDataSite foundDataSite = new FoundDataSite();

    private TotalSearchResult totalSearchResult = new TotalSearchResult();

    private final TreeMap<Integer,LinkedHashSet<SearchResultAnswer>> results = new TreeMap<>();

    private final List<Boolean> resultLoadData = new ArrayList<>();

    public static final AtomicBoolean START_INDEXING = new AtomicBoolean(FixedValue.FALSE);

    public static volatile String STATUS = FixedValue.INDEXING_NOT_STARTED;

    private String lastSearchWord = "";

    private final AtomicInteger countThreadSearch = new AtomicInteger();

    @Override
    public void startIndexing() {
        log.info("Init method startIndexing. {}", this.getClass().getName());
        START_INDEXING.set(FixedValue.TRUE);
        STATUS = FixedValue.IN_PROGRESS;
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
        getManagementRepository().delete(url);
        getManagementRepository().saveParentSites(List.of(FixedValue.getNewModelParentSite(url, name)));
        getManagementRepository().saveFoundSites(List.of(FixedValue.getNewModelSite(url, url, name)));
    }

    @Override
    public List<ModelSite> showAllSites() {
        log.info("Init method showAllSites. {}", this.getClass().getName());
        return getManagementRepository().showIndexedSites();
    }

    @Override
    public List<ModelWord> showAllWords() {
        log.info("Init method showAllWords. {}", this.getClass().getName());
        return getManagementRepository().showIndexedWords();
    }

    @Override
    public TotalSearchResult findByWord(ModelSearch modelSearch) {
        log.info("Init method searching word: {}. {}", modelSearch.getWord(), this.getClass().getName());
        List<ModelWord> words = getManagementRepository().findModelWords(modelSearch.getWord());
        if (words.size() < FixedValue.COUNT_THREADS)
        { getCountThreadSearch().set(words.size()); }
        else { getCountThreadSearch().set(FixedValue.COUNT_THREADS); }
        if (!Objects.equals(getLastSearchWord(), modelSearch.getWord())) {
            setLastSearchWord(modelSearch.getWord());
            if (!getResults().isEmpty()) { getResults().clear(); }
            for (int i = 0; i < 12; i++) { getResults().put(i, new LinkedHashSet<>()); }
            initMultithreadingSearch(modelSearch,words);
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
            } while (!Objects.equals(getResultLoadData().size(), getCountThreadSearch().get()));
            getTotalSearchResult().setResult(FixedValue.TRUE);
            getTotalSearchResult().setError(FixedValue.ERROR);
            getTotalSearchResult().setData(new ArrayList<>());
            getResults().values().forEach(sites -> getTotalSearchResult().getData()
                    .addAll(sites));
            getTotalSearchResult().setCount(getTotalSearchResult().getData().size());
            getTotalSearchResult().setData(getTotalSearchResult().getData().subList(FixedValue.ZERO,
                    getTotalSearchResult().getData().size() > modelSearch.getLimit() ? modelSearch.getLimit() :
                            getTotalSearchResult().getData().size()));
        }
        return getTotalSearchResult();
    }

    @Override
    public StatisticsResponse getStatistics() {
        log.info("Init method getStatistics. {}", this.getClass().getName());
        return getStatisticBuilder().getStatistics(getManagementRepository().getParentSites());
    }

    private void initIndexing() {
        if (getManagementRepository().countFoundSites() < FixedValue.COUNT_THREADS) {
            if (Objects.equals(getManagementRepository().countFoundSites(), FixedValue.ZERO)) {
                initStartedListSites();
            }
            DataIndexingEngine indexingEngine = new DataIndexingEngine(getManagementRepository(), getFoundDataSite());
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
        } while (searchingThreads.size() < getCountThreadSearch().get());
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
        return new Thread(new DataIndexingEngine(getManagementRepository(), getFoundDataSite()));
    }

    private Thread getNewThreadSearching(ModelSearch modelSearch, List<ModelWord> words, Integer indexTread) {
        return new Thread(new DataSearchEngine(
                getResults(),
                getResultLoadData(),
                words,
                modelSearch,
                indexTread));
    }

    private void initStartedListSites() {
        List<ModelSite> foundSiteEntities = new ArrayList<>();
        getManagementRepository().getParentSites().forEach(parent ->
                foundSiteEntities.add(FixedValue.getNewModelSite(parent.getUrl(), parent.getUrl(), parent.getName())));
        foundSiteEntities.forEach(site -> getManagementRepository().delete(site.parentUrl()));
        getManagementRepository().saveFoundSites(foundSiteEntities);
    }

    @EventListener(ApplicationStartedEvent.class)
    private void initStatistics() {
        log.info("Init statistics.");
        if (!getSitesList().getSites().isEmpty()) {
            List<ModelParentSite> parentSiteEntities = new ArrayList<>();
            getSitesList().getSites().forEach(site -> {
                ModelParentSite parentSite = FixedValue.getNewModelParentSite(site.getUrl(), site.getName());
                parentSiteEntities.add(parentSite);
            });
            getManagementRepository().saveParentSites(parentSiteEntities);
        }
    }
}

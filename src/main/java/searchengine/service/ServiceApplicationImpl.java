package searchengine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;
import searchengine.dto.model.TotalSearchResult;
import searchengine.dto.model.ModelFinder;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

@Service
@Slf4j
public class ServiceApplicationImpl implements ServiceApplication {

    private final ServiceAdditions management;

    private final ServiceBuildingData indexingManager;

    private final ServiceFindingData scanManager;

    @Autowired
    public ServiceApplicationImpl(ServiceAdditions management, ServiceBuildingData indexingManager, ServiceFindingData scanManager) {
        this.management = management;
        this.indexingManager = indexingManager;
        this.scanManager = scanManager;
    }

    @Override
    public ModelStart startIndexing() {
        log.info("Init method startIndexing in data base. {}", this.getClass().getName());
        return indexingManager.start();
    }

    @Override
    public ModelStop stopIndexing() {
        log.info("Init method stopIndexing in data base. {}", this.getClass().getName());
        return indexingManager.stop();
    }

    @Override
    public boolean addSite(String url, String name) {
        log.info("Init method addSite in data base. {}", this.getClass().getName());
        return management.addSite(url, name);
    }

    @Override
    public TotalSearchResult findByWord(ModelFinder modelFinder) {
        log.info("Init method findByWord in data base. {}", this.getClass().getName());
        return scanManager.findByWord(modelFinder);
    }

    @Override
    public StatisticsResponse getStatistics() {
        log.info("Init method getStatistics in data base. {}", this.getClass().getName());
        return management.getStatistics();
    }

    @Override
    public List<ModelSite> showAllSites() {
        log.info("Init method showAllSites in data base. {}", this.getClass().getName());
        return management.showAllSites();
    }

    @Override
    public List<ModelWord> showAllWords() {
        log.info("Init method showAllWords in data base. {}", this.getClass().getName());
        return management.showAllWords();
    }
}

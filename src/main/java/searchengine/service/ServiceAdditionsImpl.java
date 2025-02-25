package searchengine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.entity.*;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.config.FixedValue;
import searchengine.logic.StatisticBuilder;
import searchengine.repository.RepositoryProject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class ServiceAdditionsImpl implements ServiceAdditions {

    private final RepositoryProject repository;

    @Autowired
    public ServiceAdditionsImpl(RepositoryProject repository) {
        this.repository = repository;
    }

    @Override
    public boolean addSite(String url, String name) {
        log.info("Init method addSite. {}", this.getClass().getName());
        if (checkUrl(url)){
            return FixedValue.FALSE;
        }
        name = url.contains("www.") ?
                url.split("/")[2].replace("www.", "") :
                url.split("/")[2];
        log.info("Name new site: {}", name);
        repository.saveParentSites(List.of(FixedValue.getNewModelParentSite(url, name)));
        return FixedValue.TRUE;
    }

    @Override
    public List<ModelSite> showAllSites() {
        log.info("Init method showAllSites. {}", this.getClass().getName());
        return repository.showIndexedSites();
    }

    @Override
    public List<ModelWord> showAllWords() {
        log.info("Init method showAllWords. {}", this.getClass().getName());
        return repository.showIndexedWords();
    }

    @Override
    public StatisticsResponse getStatistics() {
        log.info("Init method getStatistics. {}", this.getClass().getName());
        return StatisticBuilder.getStatistics(repository.takeParentSites());
    }

    private boolean checkUrl(String url){
        try {
            URL checkUrl = new URL(url);
            InputStream inputStream = checkUrl.openStream();
            inputStream.close();
        } catch (IOException e) {
            log.info("Invalid link! \"{}\"", url);
            return FixedValue.TRUE;
        } finally {
            log.info("Url checked for errors!");
        }
        return FixedValue.FALSE;
    }
}
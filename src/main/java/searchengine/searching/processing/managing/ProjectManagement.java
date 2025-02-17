package searchengine.searching.processing.managing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import searchengine.config.SitesList;
import searchengine.dto.entity.*;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.processing.statistic.StatisticBuilder;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Component
@Slf4j
public final class ProjectManagement implements AppManagementImpl {

    private final AppManagementRepositoryImpl managementRepository;

    private final SitesList sitesList;

    @Autowired
    public ProjectManagement(AppManagementRepositoryImpl managementRepository, SitesList sitesList) {

        this.managementRepository = managementRepository;

        this.sitesList = sitesList;

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

        managementRepository.delete(url);

        managementRepository.saveParentSites(List.of(FixedValue.getNewModelParentSite(url, name)));

        managementRepository.saveFoundSites(List.of(new ModelSite(url, url, name)));

        return FixedValue.TRUE;
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
    public StatisticsResponse getStatistics() {

        log.info("Init method getStatistics. {}", this.getClass().getName());

        return StatisticBuilder.getStatistics(managementRepository.getParentSites());
    }

    @EventListener(ApplicationStartedEvent.class)
    private void initParentSites() {

        if (!sitesList.getSites().isEmpty()) {

            List<ModelParentSite> parentSiteEntities = new ArrayList<>();

            List<ModelSite> foundSiteEntities = new ArrayList<>();

            sitesList.getSites().forEach(site -> {

                ModelParentSite parentSite = FixedValue.getNewModelParentSite(site.getUrl(), site.getName());

                ModelSite modelSite = new ModelSite(site.getUrl(), site.getUrl(), site.getName());

                parentSiteEntities.add(parentSite);

                foundSiteEntities.add(modelSite);
            });

            managementRepository.saveParentSites(parentSiteEntities);

            managementRepository.saveFoundSites(foundSiteEntities);
        }
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

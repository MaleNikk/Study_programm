package searchengine.searching.processing;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.entity.SiteEntity;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.logic.AppWorker;
import searchengine.searching.injections.management.AppRepository;
import searchengine.searching.injections.management.AppStatistics;
import searchengine.searching.injections.storage.AppBasicSiteRepository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Data
public class ApplicationWorker implements AppWorker {

    @Autowired
    private AppLogic appLogic;

    @Autowired
    private AppRepository repository;

    @Autowired
    private AppBasicSiteRepository basicSiteRepository;

    @Autowired
    private AppStatistics appStatistics;

    private AtomicInteger countPools;

    private AtomicLong timeStart = new AtomicLong(System.currentTimeMillis());

    @SneakyThrows
    @Override
    public void startWork() {
        log.info("Init recursive search engine!");
        if (!getBasicSiteRepository().findAll().isEmpty()) {
            getAppStatistics().initStatistics();
            getAppLogic().setStatus(ApplicationStatics.IN_PROGRESS);
            getBasicSiteRepository().findAll()
                    .forEach(basicSite ->
                            getRepository()
                                    .saveSite(
                                            new SiteEntity(
                                                    basicSite.getUrl(),
                                                    basicSite.getUrl(),
                                                    basicSite.getName())));
            setCountPools(new AtomicInteger(ApplicationStatics.COUNT_THREADS));
            startingPoolThread();
        }
    }

    private void startingPoolThread() {
        if (getRepository().getCountNewSite() > ApplicationStatics.APP_ZERO  && getAppLogic().getStop()) {
            if (getRepository().getCountNewSite() < ApplicationStatics.START_SAVE_SITE) {
                SiteEntity site = getRepository().showAllSites().iterator().next();
                getAppLogic().buildDataSites(site);
                startingPoolThread();
            } else {
                while (getCountPools().get() > ApplicationStatics.APP_ZERO) {
                    Thread thread = new Thread(() -> {
                        dataBuilder();
                        log.info("Indexing complete!");
                        getAppLogic().setStatus(ApplicationStatics.INDEXING_COMPLETE);
                        Thread.currentThread().interrupt();
                    });
                    thread.start();
                    getCountPools().getAndDecrement();
                }
            }
        }
    }

    private void dataBuilder() {
        log.info("Init thread: {}", Thread.currentThread().getName());
        do {
            SiteEntity nextSite = getRepository().showAllSites().stream().findFirst().get();
            getAppLogic().buildDataSites(nextSite);
        } while (!getRepository().showAllSites().isEmpty() && getAppLogic().getStop());
        log.info("{}: stopped!", Thread.currentThread().getName());
        Thread.currentThread().interrupt();
    }
}

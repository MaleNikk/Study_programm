package searchengine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.AppProperties;
import searchengine.dto.entity.ModelSite;
import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;
import searchengine.config.FixedValue;
import searchengine.logic.LemmaCreator;
import searchengine.logic.ScanDataSite;
import searchengine.logic.ThreadBuilderData;
import searchengine.repository.RepositoryProject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ServiceBuildingDataImpl implements ServiceBuildingData {

    private final RepositoryProject repository;

    private final LemmaCreator morphology;

    private final ScanDataSite dataSite;

    private final ConcurrentLinkedQueue<ModelSite> sites;

    private final AppProperties properties;

    public static final AtomicBoolean START = new AtomicBoolean(FixedValue.FALSE);

    @Autowired
    public ServiceBuildingDataImpl(RepositoryProject repository, LemmaCreator morphology,
                                   ScanDataSite dataSite, AppProperties properties) {
        this.morphology = morphology;
        this.dataSite = dataSite;
        this.repository = repository;
        this.properties = properties;
        this.sites = new ConcurrentLinkedQueue<>();
    }

    @Override
    public ModelStart start() {
        log.info("Start indexing data, class: {}", this.getClass().getName());
        if (!START.get()) {
            START.set(FixedValue.TRUE);
            sites.addAll(repository.takeFoundSites());
            initThreads();
        }
        return new ModelStart(START.get());
    }

    @Override
    public ModelStop stop() {
        log.info("Stop indexing data, class: {}", this.getClass().getName());
        if (START.get()) {
            START.set(FixedValue.FALSE);
        }
        return new ModelStop(!START.get(), FixedValue.INDEXED_STOP);
    }

    private void initThreads() {
        List<Thread> listThreads = new ArrayList<>();
        Thread thread = new ThreadBuilderData(repository, dataSite, morphology, properties, sites, listThreads);
        listThreads.add(thread);
        thread.start();
    }
}

package searchengine.searching.processing.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.ModelSite;
import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public final class IndexingThreadsManager implements IndexingManager {

    private final AppManagementRepositoryImpl managementRepository;

    private final AtomicInteger countThreads = new AtomicInteger(FixedValue.ZERO);

    public static final AtomicBoolean START = new AtomicBoolean(FixedValue.FALSE);

    public static volatile String STATUS = FixedValue.INDEXING_NOT_STARTED;

    @Autowired
    public IndexingThreadsManager(AppManagementRepositoryImpl managementRepository) {
        this.managementRepository = managementRepository;
    }

    @Override
    public ModelStart start() {
        if (!START.get()) {
            if (Objects.equals(STATUS, FixedValue.INDEXING_COMPLETE)) {
                initStartedListSites();
            }
            START.set(FixedValue.TRUE);
            STATUS = FixedValue.IN_PROGRESS;
            countThreads.set(FixedValue.ZERO);
            new Thread(new DataIndexingEngine(managementRepository, countThreads)).start();
            countThreads.getAndIncrement();

        }
        return new ModelStart(START.get());
    }

    @Override
    public ModelStop stop() {
        if (START.get()) {
            START.set(FixedValue.FALSE);
            STATUS = FixedValue.INDEXED_STOP;
        }
        return new ModelStop(!START.get(), FixedValue.INDEXED_STOP);
    }

    private void initStartedListSites() {
        List<ModelSite> foundSiteEntities = new ArrayList<>();
        managementRepository.getParentSites().forEach(parent ->
                foundSiteEntities.add(FixedValue.getNewModelSite(parent.url(), parent.url(), parent.name())));
        foundSiteEntities.forEach(site -> managementRepository.delete(site.parentUrl()));
        managementRepository.saveFoundSites(foundSiteEntities);
    }
}

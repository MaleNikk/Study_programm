package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import searchengine.dto.entity.ParentSiteEntity;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.processing.ProjectManagement;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public final class TotalStatistics {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean indexing;

    private TotalStatistics(){}

    public static TotalStatistics calculate(List<ParentSiteEntity> parentSites){
        AtomicInteger sites = new AtomicInteger(parentSites.size());
        AtomicInteger pages = new AtomicInteger(FixedValue.ZERO);
        AtomicInteger lemmas = new AtomicInteger(FixedValue.ZERO);
        AtomicBoolean indexing = new AtomicBoolean(ProjectManagement.START_INDEXING.get());
        parentSites.forEach(site -> {
            pages.set(pages.get() + site.getModelParentSite().getPages());
            lemmas.set(lemmas.get() + site.getModelParentSite().getLemmas());
        });
        return new TotalStatistics(sites.get(),pages.get(),lemmas.get(),indexing.get());
    }
}

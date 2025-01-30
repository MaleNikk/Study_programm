package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import searchengine.dto.entity.ModelParentSite;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.processing.indexing.IndexingThreadsManager;

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

    public static TotalStatistics calculate(List<ModelParentSite> parentSites){
        AtomicInteger sites = new AtomicInteger(parentSites.size());
        AtomicInteger pages = new AtomicInteger(FixedValue.ZERO);
        AtomicInteger lemmas = new AtomicInteger(FixedValue.ZERO);
        AtomicBoolean indexing = new AtomicBoolean(IndexingThreadsManager.START.get());
        parentSites.forEach(site -> {
            pages.set(pages.get() + site.pages());
            lemmas.set(lemmas.get() + site.lemmas());
        });
        return new TotalStatistics(sites.get(),pages.get(),lemmas.get(),indexing.get());
    }
}

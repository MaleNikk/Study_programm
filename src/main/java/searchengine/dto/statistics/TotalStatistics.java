package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import searchengine.dto.entity.ModelParentSite;
import searchengine.config.FixedValue;
import searchengine.service.ManagementBuildingData;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public final class TotalStatistics {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean indexing;

    public static TotalStatistics calculate(List<ModelParentSite> parentSites){
        AtomicInteger sites = new AtomicInteger(parentSites.size());
        AtomicInteger pages = new AtomicInteger(FixedValue.ZERO);
        AtomicInteger lemmas = new AtomicInteger(FixedValue.ZERO);
        parentSites.forEach(site -> {
            pages.set(pages.get() + site.getPages());
            lemmas.set(lemmas.get() + site.getLemmas());
        });
        return new TotalStatistics(sites.get(),pages.get(),lemmas.get(), ManagementBuildingData.START.get());
    }
}

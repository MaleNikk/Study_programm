package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import searchengine.dto.entity.ModelParentSite;
import searchengine.searching.processing.indexing.IndexingThreadsManager;

@Data
@AllArgsConstructor
public final class DetailedStatisticsItem {
    private String url;
    private String name;
    private String status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;

    private DetailedStatisticsItem(){}

    public static DetailedStatisticsItem from(ModelParentSite modelParentSite){
        return new DetailedStatisticsItem(
                modelParentSite.url(),
                modelParentSite.name(),
                IndexingThreadsManager.STATUS,
                modelParentSite.statusTime(),
                modelParentSite.error(),
                modelParentSite.pages(),
                modelParentSite.lemmas());
    }
}

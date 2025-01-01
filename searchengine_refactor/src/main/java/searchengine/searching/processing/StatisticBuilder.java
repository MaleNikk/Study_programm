package searchengine.searching.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import searchengine.dto.entity.ModelParentSite;
import searchengine.dto.entity.ParentSiteEntity;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
public class StatisticBuilder {

    private final HashMap<Integer,DetailedStatisticsItem> detailedStatistics = new HashMap<>();

    private final TotalStatistics totalStatistics =
            new TotalStatistics(0,0,0, FixedValue.TRUE);

    private final StatisticsData statisticsData = new StatisticsData(getTotalStatistics(),
            new ArrayList<>());

    private final StatisticsResponse statisticsResponse = new StatisticsResponse(FixedValue.TRUE,getStatisticsData());

    public StatisticsResponse getStatistics(List<ParentSiteEntity> parentSiteEntities){
        log.info("Init method getStatistics. {}", this.getClass().getName());
            parentSiteEntities.forEach(site -> {
                ModelParentSite parent = site.getModelParentSite();
                Integer key = site.getId();
                if (!getDetailedStatistics().containsKey(key)){
                    getDetailedStatistics().put(key,initNewDetailed(parent));
                } else {
                    getDetailedStatistics().get(key).setPages(parent.getPages());
                    getDetailedStatistics().get(key).setLemmas(parent.getLemmas());
                    getDetailedStatistics().get(key).setStatus(parent.getStatus());
                }
                    });

            getStatisticsData().setDetailed(getDetailedStatistics().values().stream().toList());
            getTotalStatistics().setSites(getDetailedStatistics().size());
            getTotalStatistics().setLemmas(FixedValue.ZERO);
            getTotalStatistics().setPages(FixedValue.ZERO);

            getDetailedStatistics().values().forEach(detailed -> {
                getTotalStatistics().setLemmas(getTotalStatistics().getLemmas() + detailed.getLemmas());
                getTotalStatistics().setPages(getTotalStatistics().getPages() + detailed.getPages());
            });

        return getStatisticsResponse();
    }

    private DetailedStatisticsItem initNewDetailed(ModelParentSite modelParentSite){
        return new DetailedStatisticsItem(
                modelParentSite.getUrl(),
                modelParentSite.getName(),
                ProjectManagement.STATUS,
                System.nanoTime(),
                FixedValue.ERROR,
                modelParentSite.getPages(),
                modelParentSite.getLemmas());
    }
}

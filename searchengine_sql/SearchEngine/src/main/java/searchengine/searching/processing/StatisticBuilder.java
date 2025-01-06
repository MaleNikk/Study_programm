package searchengine.searching.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import searchengine.dto.entity.ModelParentSite;
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

    public StatisticsResponse getStatistics(List<ModelParentSite> parentSiteEntities){
        log.info("Init method getStatistics. {}", this.getClass().getName());
            parentSiteEntities.forEach(site -> {
                Integer key = site.getUrl().hashCode();
                if (!getDetailedStatistics().containsKey(key)){
                    getDetailedStatistics().put(key,initNewDetailed(site));
                } else {
                    getDetailedStatistics().get(key).setPages(site.getPages());
                    getDetailedStatistics().get(key).setLemmas(site.getLemmas());
                    getDetailedStatistics().get(key).setStatus(site.getStatus());
                    getDetailedStatistics().get(key).setStatus(ProjectManagement.STATUS);
                    getDetailedStatistics().get(key).setStatusTime(System.currentTimeMillis());
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
                System.currentTimeMillis(),
                FixedValue.ERROR,
                modelParentSite.getPages(),
                modelParentSite.getLemmas());
    }
}

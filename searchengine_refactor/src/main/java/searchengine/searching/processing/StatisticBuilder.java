package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import searchengine.dto.entity.ParentSiteEntity;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;

import java.util.List;

@Slf4j
public class StatisticBuilder {

    public StatisticsResponse getStatistics(List<ParentSiteEntity> parentSites){
        log.info("Init method getStatistics. {}", this.getClass().getName());
        List<DetailedStatisticsItem> statisticsItems = parentSites.stream()
                .map(entity -> DetailedStatisticsItem.from(entity.getModelParentSite())).toList();
        return new StatisticsResponse(
                FixedValue.TRUE, new StatisticsData(TotalStatistics.calculate(parentSites), statisticsItems));
    }
}

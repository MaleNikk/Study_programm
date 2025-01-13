package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import searchengine.dto.entity.ModelParentSite;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;

import java.util.List;

@Slf4j
public final class StatisticBuilder {

    public static StatisticsResponse getStatistics(List<ModelParentSite> parentSites){
        log.info("Init method getStatistics. Statistic builder class.");
        List<DetailedStatisticsItem> statisticsItems = parentSites.stream().map(DetailedStatisticsItem::from).toList();
        return new StatisticsResponse(
                FixedValue.TRUE, new StatisticsData(TotalStatistics.calculate(parentSites), statisticsItems));
    }
}

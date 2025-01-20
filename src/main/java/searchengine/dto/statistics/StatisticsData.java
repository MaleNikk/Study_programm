package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class StatisticsData {
    private TotalStatistics total;
    private List<DetailedStatisticsItem> detailed;

    private StatisticsData(){}
}

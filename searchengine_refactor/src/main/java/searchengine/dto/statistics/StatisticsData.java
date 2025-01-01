package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticsData {
    private TotalStatistics total;
    private List<DetailedStatisticsItem> detailed;
}

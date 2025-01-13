package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class StatisticsResponse {
    private boolean result;
    private StatisticsData statistics;

    private StatisticsResponse(){}
}

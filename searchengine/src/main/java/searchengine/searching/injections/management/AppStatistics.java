package searchengine.searching.injections.management;

import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.statistics.StatisticsResponse;

public interface AppStatistics {

    void initStatistics();

    void buildStatistics(RegisteredSite registeredSite);

    StatisticsResponse getStatistics();
}

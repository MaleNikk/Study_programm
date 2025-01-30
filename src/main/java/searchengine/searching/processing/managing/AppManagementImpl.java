package searchengine.searching.processing.managing;

import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

public interface AppManagementImpl {

    void addSite(String url, String name);

    List<ModelSite> showAllSites();

    List<ModelWord> showAllWords();

    StatisticsResponse getStatistics();
}

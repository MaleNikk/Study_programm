package searchengine.searching.service;

import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;
import searchengine.dto.model.TotalSearchResult;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

public interface AppServiceImpl {
    ModelStart startIndexing();

    ModelStop stopIndexing();

    void addSite(String url, String name);

    List<ModelSite> showAllSites();

    List<ModelWord> showAllWords();

    TotalSearchResult findByWord(ModelSearch modelSearch);

    StatisticsResponse getStatistics();
}

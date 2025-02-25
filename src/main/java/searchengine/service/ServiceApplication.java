package searchengine.service;

import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;
import searchengine.dto.model.TotalSearchResult;
import searchengine.dto.model.ModelFinder;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

public interface ServiceApplication {
    ModelStart startIndexing();

    ModelStop stopIndexing();

    boolean addSite(String url, String name);

    List<ModelSite> showAllSites();

    List<ModelWord> showAllWords();

    TotalSearchResult findByWord(ModelFinder modelFinder);

    StatisticsResponse getStatistics();
}

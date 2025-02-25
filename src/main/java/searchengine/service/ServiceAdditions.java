package searchengine.service;

import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.statistics.StatisticsResponse;

import java.util.List;

public interface ServiceAdditions {

    boolean addSite(String url, String name);

    List<ModelSite> showAllSites();

    List<ModelWord> showAllWords();

    StatisticsResponse getStatistics();
}

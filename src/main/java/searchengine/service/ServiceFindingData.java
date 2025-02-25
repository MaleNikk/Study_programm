package searchengine.service;

import searchengine.dto.model.ModelFinder;
import searchengine.dto.model.TotalSearchResult;

public interface ServiceFindingData {
    TotalSearchResult findByWord(ModelFinder modelFinder);
}

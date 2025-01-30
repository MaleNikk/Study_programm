package searchengine.searching.processing.searching;

import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.TotalSearchResult;

public interface SearchingManager {
    TotalSearchResult findByWord(ModelSearch modelSearch);
}

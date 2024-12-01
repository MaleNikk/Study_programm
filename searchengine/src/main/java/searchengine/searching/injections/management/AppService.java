package searchengine.searching.injections.management;

import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.entity.WordEntity;
import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;

import java.util.List;

public interface AppService {

    void startIndexing();

    void stopIndexing(boolean command);

    void addSite(String url, String name);

    ModelAnswerResponse findByWord(ModelSearch modelSearch);

    List<RegisteredSite> showAllSites();

    List<WordEntity> showAllWords();
}

package searchengine.searching.injections.management;

import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;

public interface AppManagement {

    void startIndexing();

    void stopIndexing(boolean command);

    void addSite(String url, String name);

    ModelAnswerResponse findByWord(ModelSearch modelSearch);
}

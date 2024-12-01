package searchengine.searching.injections.logic;

import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;

public interface AppSearching {

    ModelAnswerResponse getModelAnswer(ModelSearch modelSearch);

    Boolean getStart();

    void setStart(Boolean command);
}

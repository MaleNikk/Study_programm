package searchengine.searching.processing.indexing;

import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;

public interface IndexingManager {

    ModelStart start();

    ModelStop stop();
}

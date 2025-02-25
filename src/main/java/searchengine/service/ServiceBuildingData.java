package searchengine.service;

import searchengine.dto.model.ModelStart;
import searchengine.dto.model.ModelStop;

public interface ServiceBuildingData {

    ModelStart start();

    ModelStop stop();
}

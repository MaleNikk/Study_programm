package searchengine.searching.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.entity.WordEntity;
import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;
import searchengine.searching.injections.management.AppManagement;
import searchengine.searching.injections.management.AppRepository;
import searchengine.searching.injections.management.AppService;

import java.util.List;

@Service
@Slf4j
@Data
public class ApplicationService implements AppService {

    @Autowired
    private AppRepository repository;

    @Autowired
    private AppManagement management;

    private Long timeStart;

    private Long timeIndexing;

    @Override
    public void startIndexing() {
        log.info("Init method startIndexing in data base. Application service class.");
        setTimeStart(System.currentTimeMillis());
        getManagement().startIndexing();
    }

    @Override
    public void stopIndexing(boolean command) {
        log.info("Init method stopIndexing in data base. Application service class.");
        getManagement().stopIndexing(command);
        setTimeIndexing((System.currentTimeMillis() - getTimeStart())/60000);
    }

    @Override
    public void addSite(String url, String name) {
        log.info("Init method addSite in data base. Application service class.");
        getManagement().addSite(url, name);
    }

    @Override
    public ModelAnswerResponse findByWord(ModelSearch modelSearch) {
        log.info("Init method findByWord in data base. Application service class.");
        return getManagement().findByWord(modelSearch);
    }

    @Override
    public List<RegisteredSite> showAllSites() {
        log.info("Init method showAllSites in data base. Application service class.");
        return getRepository().showAllRegisteredSites();
    }

    @Override
    public List<WordEntity> showAllWords() {
        log.info("Init method showAllWords in data base. Application service class.");
        return getRepository().showAllWords();
    }
}

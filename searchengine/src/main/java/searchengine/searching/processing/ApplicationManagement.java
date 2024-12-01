package searchengine.searching.processing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.entity.BasicSiteEntity;
import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.logic.AppSearching;
import searchengine.searching.injections.logic.AppWorker;
import searchengine.searching.injections.management.AppManagement;
import searchengine.searching.injections.management.AppRepository;
import searchengine.searching.injections.storage.AppBasicSiteRepository;

import java.time.LocalDateTime;

@Data
@Slf4j
public class ApplicationManagement  implements AppManagement {

    @Autowired
    private AppLogic appLogic;

    @Autowired
    private AppWorker appWorker;

    @Autowired
    private AppSearching appSearching;

    @Autowired
    private AppRepository repository;

    @Autowired
    private AppBasicSiteRepository basicSiteRepository;

    @Override
    public void startIndexing() {
        log.info("Init method startIndexing. ApplicationManagement class.");
        getAppLogic().stop(ApplicationStatics.TRUE);
        getAppWorker().startWork();
    }

    @Override
    public void stopIndexing(boolean command) {
        log.info("Init method stopIndexing. ApplicationManagement class.");
        getAppLogic().stop(ApplicationStatics.FALSE);
    }

    @Override
    public void addSite(String url, String name) {
        log.info("Init method addSite. ApplicationManagement class.");
        getBasicSiteRepository().save(new BasicSiteEntity(url,name, LocalDateTime.now()));
        getAppWorker().startWork();
    }

    @Override
    public ModelAnswerResponse findByWord(ModelSearch modelSearch) {
        log.info("Init method searching word: {}. ApplicationManagement class.", modelSearch.getWord());
        if (getAppSearching().getStart()){
            getAppSearching().setStart(false);
            log.info("Searching shutdown.");
        }
        return getAppSearching().getModelAnswer(modelSearch);
    }
}

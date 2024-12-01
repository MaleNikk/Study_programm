package searchengine.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.entity.WordEntity;
import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.ModelStartStop;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.management.AppService;
import searchengine.searching.injections.management.AppStatistics;
import searchengine.searching.processing.ApplicationConnection;
import searchengine.searching.processing.ApplicationStatics;

import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AppService service;

    @Autowired
    private AppLogic appLogic;

    @Autowired
    private AppStatistics appStatistics;


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        log.info("Init statistics at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(getAppStatistics().getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<ModelStartStop> startIndexing() {
        log.info("Init start indexing at system time: {}", System.currentTimeMillis());
        if (!getAppLogic().getStop()){
            getService().startIndexing();
            return ResponseEntity.ok(new ModelStartStop(ApplicationStatics.TRUE, "Indexing start!"));
        } else {
            return ResponseEntity.ok(new ModelStartStop(ApplicationStatics.FALSE, "Indexing in progress!"));
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<ModelStartStop> stopIndexing() {
        log.info("Init stop indexing at system time: {}", System.currentTimeMillis());
        if (getAppLogic().getStop() && getAppLogic().getStatus().equals(ApplicationStatics.IN_PROGRESS)){
            getService().stopIndexing(ApplicationStatics.FALSE);
            return ResponseEntity.ok(new ModelStartStop(ApplicationStatics.TRUE, "Indexing stop!"));
        } else {
            return ResponseEntity.ok(new ModelStartStop(ApplicationStatics.FALSE, "Indexing not started!"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ModelAnswerResponse> search(@RequestParam String query, String site, Integer offset, Integer limit) {
        log.info("Init search word; {} at system time: {}",query, System.currentTimeMillis());
        ModelAnswerResponse modelAnswerResponse;

        if (!query.isBlank()) {
            ModelSearch modelSearch = new ModelSearch(query, site, offset, limit);
            if (modelSearch.getParentSite() == null) {
                modelSearch.setParentSite("all");
            }
            if (modelSearch.getLimit() == 10) {
                modelSearch.setLimit(3);
            }
            modelAnswerResponse = getService().findByWord(modelSearch);
            return ResponseEntity.ok(modelAnswerResponse);
        }
        return ResponseEntity.badRequest().body(ModelAnswerResponse.getBadResponse());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<ModelAnswerResponse> indexing(@RequestParam String url) {
        log.info("Init add site for indexing at system time: {}", System.currentTimeMillis());
        if (ApplicationConnection.checkSite(url)) {
            String name = url.split(ApplicationStatics.REGEX_CHECK_LINK)[1];
            getService().addSite(url,name);
         return ResponseEntity.ok(ModelAnswerResponse.getOkResponse());
        }
        return ResponseEntity.badRequest().body(ModelAnswerResponse.getBadResponse());
    }

    @GetMapping("/words")
    public ResponseEntity<List<WordEntity>> showAllWords() {
        log.info("Init show all words at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(getService().showAllWords().subList(0, 25));
    }

    @GetMapping("/sites")
    public ResponseEntity<List<RegisteredSite>> showAllSites() {
        log.info("Init show all sites at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(getService().showAllSites().subList(0, 10));
    }
}

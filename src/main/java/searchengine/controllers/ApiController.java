package searchengine.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.TotalSearchResult;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.ModelStartStop;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.processing.ProjectManagement;
import searchengine.searching.service.AppServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public final class ApiController {

    private final AppServiceImpl service;

    @Autowired
    public ApiController(AppServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        log.info("Init statistics at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<ModelStartStop> startIndexing() {
        log.info("Init start indexing at system time: {}", System.currentTimeMillis());
        if (!ProjectManagement.START_INDEXING.get()) {
            service.startIndexing();
            return ResponseEntity.ok(FixedValue.startIndexing());
        } else {
            return ResponseEntity.ok(FixedValue.wasStarted());
        }
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<ModelStartStop> stopIndexing() {
        log.info("Init stop indexing at system time: {}", System.currentTimeMillis());
        if (ProjectManagement.START_INDEXING.get() && ProjectManagement.STATUS.equals(FixedValue.IN_PROGRESS)) {
            service.stopIndexing();
            return ResponseEntity.ok(FixedValue.stopIndexing());
        } else {
            return ResponseEntity.ok(FixedValue.notStarted());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<TotalSearchResult> search(@RequestParam String query, String site, Integer offset, Integer limit) {
        log.info("Init search word; {} at system time: {}. Options: parent site - {}, offset - {}, limit - {}",
                query, System.nanoTime(), site, offset, limit);
        if (!query.isBlank()) {
            if (site == null) { site = FixedValue.SEARCH_IN_ALL; }
            ModelSearch modelSearch = new ModelSearch(query, site, offset, limit);
            return ResponseEntity.ok(service.findByWord(modelSearch));
        } else {
            return ResponseEntity.badRequest().body(FixedValue.getBadResponse());
        }
    }

    @PostMapping("/indexPage")
    public ResponseEntity<TotalSearchResult> indexing(@RequestParam String url) {
        log.info("Init add site for indexing at system time: {}", System.currentTimeMillis());
        if (!url.isBlank()) {
            service.addSite(url, "");
            log.info("Site added, url: {}", url);
            return ResponseEntity.ok(FixedValue.getOkResponse());
        } else {
            return ResponseEntity.badRequest().body(FixedValue.getBadResponseAddSite());
        }
    }

    @GetMapping("/words")
    public ResponseEntity<List<ModelWord>> showAllWords() {
        log.info("Init show all words at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.showAllWords());
    }

    @GetMapping("/sites")
    public ResponseEntity<List<ModelSite>> showAllSites() {
        log.info("Init show all sites at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.showAllSites());
    }
}

package searchengine.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.entity.ModelSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.*;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.config.FixedValue;
import searchengine.service.ServiceApplication;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public final class ApiController {

    private final ServiceApplication service;

    @Autowired
    public ApiController(ServiceApplication service) {
        this.service = service;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        log.info("Init statistics at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<ModelStart> startIndexing() {
        log.info("Init start indexing at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.startIndexing());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<ModelStop> stopIndexing() {
        log.info("Init stop indexing at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.stopIndexing());
    }

    @GetMapping("/search")
    public ResponseEntity<TotalSearchResult> search(@RequestParam String query,String site,Integer offset,Integer limit
    ) {
        log.info("Init search word; {} at system time: {}. Options: parent site - {}, offset - {}, limit - {}",
                query, System.nanoTime(), site, offset, limit);
        if (query.isBlank()) {
            return ResponseEntity.badRequest().body(FixedValue.getBadResponse());
        }
        return ResponseEntity.ok(service.findByWord(new ModelFinder(query, site, offset, limit)));
    }

    @PostMapping("/indexPage")
    public ResponseEntity<ModelQueryAnswer> addPageForIndexing(@RequestParam String url) {
        log.info("Init add site \"{}\" for indexing at system time: {}",url, System.currentTimeMillis());
        if (service.addSite(url, "")) {
            log.info("Site added, url: {}", url);
            return ResponseEntity.ok(FixedValue.getOkResponse());
        }
        return ResponseEntity.badRequest().body(FixedValue.getBadResponseAddSite());
    }

    @GetMapping("/words")
    public ResponseEntity<List<ModelWord>> showSomeWords() {
        log.info("Init show all words at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.showAllWords());
    }

    @GetMapping("/sites")
    public ResponseEntity<List<ModelSite>> showSomeSites() {
        log.info("Init show all sites at system time: {}", System.currentTimeMillis());
        return ResponseEntity.ok(service.showAllSites());
    }
}

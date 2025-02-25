package searchengine.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.config.AppProperties;
import searchengine.dto.entity.*;
import searchengine.config.FixedValue;
import searchengine.repository.RepositoryProject;
import searchengine.service.ServiceBuildingDataImpl;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
public class ThreadBuilderData extends Thread {

    private final RepositoryProject repository;

    private final ScanDataSite dataSite;

    private final LemmaCreator morphology;

    private final AppProperties properties;

    private ConcurrentLinkedQueue<ModelSite> sites;

    private List<Thread> threads;

    @Override
    public void run() {
        log.info("Init thread for indexing.");
        while (ServiceBuildingDataImpl.START.get()) {
            if (!sites.isEmpty()) {
                ModelSite modelSite = takeEntity();
                if (threads.size() < properties.getActiveThreads() && sites.size() > properties.getActiveThreads()) {
                    Thread thread = new ThreadBuilderData(repository, dataSite, morphology, properties, sites, threads);
                    threads.add(thread);
                    thread.start();
                }
                startBuildDataSite(modelSite);
            } else {
                List<ModelSite> modelSites = repository.takeFoundSites();
                if (modelSites.isEmpty()) {
                    break;
                }
                sites.addAll(modelSites);
            }
        }
        stopThread();
    }

    private void startBuildDataSite(ModelSite modelSite) {
        if (checkUrl(modelSite.url())) {
            repository.saveSystemSite(modelSite);
        } else {
            Document document = dataSite.initDocument(modelSite.url());
            if (document != null) {
                finishBuildDataSite(document, modelSite);
            } else {
                repository.saveBadSite(modelSite);
            }

        }
    }

    private void finishBuildDataSite(Document document, ModelSite site) {
        HashSet<String> links = dataSite.sitePages(document);
        repository.saveStatistics(site.parentUrl(),
                saveWords(dataSite.textForDataBase(document), site),
                saveFoundSites(links, site), FixedValue.IN_PROGRESS);
    }

    private int saveFoundSites(HashSet<String> links, ModelSite modelSite) {
        repository.saveFoundSites(links.stream()
                .map(link -> new ModelSite(link, modelSite.parentUrl(), modelSite.name())).toList());
        return links.size();
    }

    private int saveWords(String text, ModelSite site) {
        String[] strings = Pattern.compile(FixedValue.REGEX_NO_ABC).split(text);
        HashSet<String> lemmas = new HashSet<>(Stream.of(strings).map(morphology::getForm).toList());
        StringBuilder builder = new StringBuilder();
        for (String lemma : lemmas) {
            builder.append("('").append(lemma).append("','").append(site.url()).append("','")
                    .append(site.parentUrl()).append("','").append(site.name()).append("','")
                    .append(text.split(lemma).length).append("'),");
        }

        if (!builder.isEmpty()) {
            builder.deleteCharAt(builder.lastIndexOf(","));
            repository.saveWords(builder.toString());
        }
        return lemmas.size();
    }

    private boolean checkUrl(String url) {
        return Pattern.compile(FixedValue.REGEX_URL).matcher(url).find();
    }

    private void stopThread() {
        log.info("Init stop thread for indexing.");
        Thread thread = threads.get(threads.indexOf(this));
        threads.remove(thread);
        thread.interrupt();
    }

    private synchronized ModelSite takeEntity(){
        return sites.poll();
    }
}

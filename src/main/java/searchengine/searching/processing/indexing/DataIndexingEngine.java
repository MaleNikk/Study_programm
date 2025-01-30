package searchengine.searching.processing.indexing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.*;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.processing.connect.FoundDataSite;
import searchengine.searching.processing.morhpology.ProjectMorphology;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Slf4j
public class DataIndexingEngine implements Runnable {

    private final AppManagementRepositoryImpl managementRepository;

    private AtomicInteger countThreads;

    private final FoundDataSite dataSite = new FoundDataSite();

    private final ProjectMorphology morphology = new ProjectMorphology();

    @Override
    public void run() {
        log.info("Start thread for indexing: {}", Thread.currentThread().getName());
        while (IndexingThreadsManager.START.get()) {
            if (managementRepository.countFoundSites() > 1 && countThreads.get() < FixedValue.COUNT_THREADS) {
                new Thread(new DataIndexingEngine(managementRepository,countThreads)).start();
                countThreads.getAndIncrement();
            }
            treatmentData();
        }
        log.info("Thread indexing stopped!");
        Thread.currentThread().interrupt();
    }

    private void treatmentData() {

        ModelSite modelSite = managementRepository.getFoundSite();

        if (modelSite != null) {
            String url = modelSite.url();
            if (checkUrl(url)) {
                managementRepository.saveSystemSite(modelSite);
            } else {
                Document document = dataSite.getDocument(url);
                if (document != null) {
                    treatmentData(document,modelSite);
                } else {
                    managementRepository.saveBadSite(modelSite);
                }
            }
        } else {
            IndexingThreadsManager.STATUS = FixedValue.INDEXING_COMPLETE;
            Thread.currentThread().interrupt();
        }
    }

    private void treatmentData(Document document, ModelSite modelSite){
        HashMap<Integer, HashSet<String>> urls = dataSite.getSiteLinks(modelSite.url());
        HashSet<String> links = urls.get(1);
        HashSet<String> words = dataSite.getSiteWords(document);
        String text = document.getAllElements().text();
        if (!urls.get(0).isEmpty()) {
            sendToSaveBadSites(urls.get(0),modelSite);
        }
        if (text.isBlank() || text.length() < 5) {
            managementRepository.saveSystemSite(modelSite);
        } else {
            managementRepository.saveStatistics(
                    modelSite.parentUrl(), words.size(), links.size(), IndexingThreadsManager.STATUS);
            if (!links.isEmpty()) {
                sendToSaveFoundSites(links,modelSite);
            }
            if (modelSite.url().toLowerCase().contains(modelSite.name().toLowerCase()) ||
                    modelSite.url().toLowerCase().contains(modelSite.parentUrl().toLowerCase())) {
                sendToSaveWords(words,modelSite);
            }
        }
    }

    private void sendToSaveBadSites(HashSet<String> links, ModelSite modelSite) {
        links.stream().map(badLink ->FixedValue.getNewModelSite(badLink, modelSite.parentUrl(), modelSite.name()))
             .forEach(managementRepository::saveBadSite);
    }

    private void sendToSaveFoundSites(HashSet<String> links, ModelSite modelSite){
        managementRepository.saveFoundSites(links.stream().map(site ->
                FixedValue.getNewModelSite(site, modelSite.parentUrl(), modelSite.name())).toList());
    }
    private void sendToSaveWords(HashSet<String> words, ModelSite modelSite){
        managementRepository.saveWord(words.stream().map(word -> new ModelWord(morphology.getForm(word),
                        word,modelSite.url(),modelSite.name(), modelSite.parentUrl()))
                .filter(modelWord -> !modelWord.lemma().isBlank()).toList());
    }

    private boolean checkUrl(String url) {
        String checkedType = url.substring(url.length() - 6, url.length() - 1);
        return (url.contains("{") ||
                url.contains("}")) ||
                url.contains("=") ||
                url.contains("?") ||
                url.contains("\\")||
                checkedType.contains(".css") ||
                checkedType.contains(".csv") ||
                checkedType.contains(".js") ||
                checkedType.contains(".png");
    }
}

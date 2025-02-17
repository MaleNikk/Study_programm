package searchengine.searching.processing.indexing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.*;
import searchengine.searching.processing.constant.FixedValue;
import searchengine.searching.processing.connect.FoundDataSite;
import searchengine.searching.processing.morhpology.ProjectMorphology;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.*;
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
                new Thread(new DataIndexingEngine(managementRepository, countThreads)).start();
                countThreads.getAndIncrement();
            }
            startBuildDataSite();
        }
        log.info("Thread indexing stopped!");
        Thread.currentThread().interrupt();
    }

    private void startBuildDataSite() {

        ModelSite modelSite = managementRepository.getFoundSite();

        if (modelSite != null) {
            if (checkUrl(modelSite.url())) {
                managementRepository.saveSystemSite(modelSite);
            } else {
                Document document = dataSite.getDocument(modelSite.url());
                if (document != null) {
                    finishBuildDataSite(document, modelSite);
                } else {
                    managementRepository.saveBadSite(modelSite);
                }
            }
        } else {
            IndexingThreadsManager.STATUS = FixedValue.INDEXING_COMPLETE;
            Thread.currentThread().interrupt();
        }
    }

    private void finishBuildDataSite(Document document, ModelSite modelSite) {

        HashSet<String> words = new HashSet<>(List.of(dataSite.getSiteText(document).split("\\s+")));

        HashSet<String> links = dataSite.getSiteLinks(document);

        if (words.isEmpty()) {

            managementRepository.saveSystemSite(modelSite);

        } else {

            managementRepository.saveStatistics(

                    modelSite.parentUrl(), words.size(), links.size(), IndexingThreadsManager.STATUS);

            if (!links.isEmpty()) {

                sendToSaveFoundSites(links, modelSite);
            }
            if (modelSite.url().toLowerCase().contains(modelSite.name().toLowerCase()) ||
                    modelSite.url().toLowerCase().contains(modelSite.parentUrl().toLowerCase())) {

                sendToSaveWords(words, modelSite);
            }
        }
    }

    private void sendToSaveFoundSites(HashSet<String> links, ModelSite modelSite) {
        managementRepository.saveFoundSites(links.stream()
                .map(link -> new ModelSite(link, modelSite.parentUrl(), modelSite.name())).toList());
    }

    private void sendToSaveWords(HashSet<String> words, ModelSite modelSite) {
        List<ModelWord> modelWords = new ArrayList<>();
        for (String word : words) {
            String lemma = morphology.getForm(word);
            if (!lemma.isBlank()) {
                modelWords.add(new ModelWord(lemma, word, modelSite.url(), modelSite.name(), modelSite.parentUrl()));
            }
        }
        managementRepository.saveWord(modelWords);
    }

    private boolean checkUrl(String url) {
        return (url.contains("{") || url.contains("}")) ||
                url.contains("=") || url.contains("?") ||
                url.contains("\\") || url.endsWith(".css") ||
                url.endsWith(".csv") || url.endsWith(".js") ||
                url.endsWith(".png");
    }
}

package searchengine.searching.processing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.*;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class DataIndexingEngine implements Runnable {

    private final AppManagementRepositoryImpl managementRepository;

    private final ProjectMorphology morphology = new ProjectMorphology();

    @Override
    public void run() {
        log.info("Start thread for indexing: {}", Thread.currentThread().getName());
        FoundDataSite foundDataSite = new FoundDataSite();
        while (ProjectManagement.START_INDEXING.get() && !ProjectManagement.STATUS.equals(FixedValue.INDEXING_COMPLETE)
                && managementRepository.countFoundSites() > FixedValue.ZERO) {
            loadData(foundDataSite);
        }
        log.info("Thread indexing stopped!");
        Thread.currentThread().interrupt();
    }

    public void loadData(FoundDataSite foundDataSite) {

        ModelSite modelSite = managementRepository.getFoundSite();


        if (modelSite != null) {
            String url = modelSite.url();
            if (foundDataSite.checkUrl(url)) {
                managementRepository.saveSystemSite(modelSite);
            } else {
                Document document = foundDataSite.getDocument(url);
                if (document != null) {
                    HashMap<Integer,HashSet<String>> urls = foundDataSite.getSiteLinks(url);
                    HashSet<String> links = urls.get(1);
                    HashSet<String> words = foundDataSite.getSiteWords(document);
                    String text = document.getAllElements().text();
                    if (!urls.get(0).isEmpty()){
                        urls.get(0)
                                .stream()
                                .map(badLink ->
                                        FixedValue.getNewModelSite(badLink,modelSite.parentUrl(),modelSite.name()))
                                .forEach(managementRepository::saveBadSite);
                    }
                    if (text.isBlank() || text.length() < 5) {
                        managementRepository.saveSystemSite(modelSite);
                    } else {
                        managementRepository.saveStatistics(
                                modelSite.parentUrl(), words.size(), links.size(), ProjectManagement.STATUS);
                        if (!links.isEmpty()) {
                            List<ModelSite> foundSiteEntities = links.stream().map(site ->
                                    FixedValue.getNewModelSite(site,modelSite.parentUrl(),modelSite.name())).toList();
                            managementRepository.saveFoundSites(foundSiteEntities);
                        }
                        if (url.toLowerCase().contains(modelSite.name().toLowerCase()) ||
                                url.toLowerCase().contains(modelSite.parentUrl().toLowerCase())) {
                            managementRepository.saveWord(words
                                    .stream()
                                    .map(word -> new ModelWord(
                                            morphology.getForm(word),
                                            word,
                                            modelSite.url(),
                                            modelSite.name(),
                                            modelSite.parentUrl()))
                                    .filter(modelWord -> !modelWord.lemma().isBlank()).toList());
                        }
                    }
                } else {
                    managementRepository.saveBadSite(modelSite);
                }
            }
        } else {
            ProjectManagement.STATUS = FixedValue.INDEXING_COMPLETE;
            Thread.currentThread().interrupt();
        }
    }
}

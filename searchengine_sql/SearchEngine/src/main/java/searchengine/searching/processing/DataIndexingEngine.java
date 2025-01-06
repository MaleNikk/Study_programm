package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.*;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.HashSet;
import java.util.List;

@Slf4j
public record DataIndexingEngine(AppManagementRepositoryImpl managementRepository,
                                 FoundDataSite foundDataSite) implements Runnable {

    @Override
    public void run() {
        log.info("Start thread for indexing: {}", Thread.currentThread().getName());
        while (ProjectManagement.START_INDEXING.get() && !ProjectManagement.STATUS.equals(FixedValue.INDEXING_COMPLETE)
                && managementRepository().countFoundSites() > FixedValue.ZERO) {
            loadData();
        }
        log.info("Thread indexing stopped!");
        Thread.currentThread().interrupt();
    }

    public void loadData() {

        ModelSite modelSite = managementRepository().getFoundSite();

        if (modelSite != null) {
            String url = modelSite.url();
            if (foundDataSite().checkUrl(url)) {
                saveSystemSite(modelSite);
            } else {
                Document document = FoundDataSite.getDocument(url);
                if (document != null) {
                    HashSet<String> links = foundDataSite().getSiteLinks(document);
                    HashSet<String> words = foundDataSite().getSiteWords(document);
                    String text = document.getAllElements().text();
                    if (text.isBlank() || text.length() < 5) {
                        saveSystemSite(modelSite);
                    } else {
                        managementRepository().saveStatistics(
                                modelSite.parentUrl(), words.size(), links.size(), ProjectManagement.STATUS);

                        if (!links.isEmpty()) {
                            List<ModelSite> foundSiteEntities = links.stream().map(site ->
                                    FixedValue.getNewModelSite(site,modelSite.parentUrl(),modelSite.name())).toList();
                            managementRepository().saveFoundSites(foundSiteEntities);
                        }
                        if (url.toLowerCase().contains(modelSite.name().toLowerCase()) ||
                                url.toLowerCase().contains(modelSite.parentUrl().toLowerCase())) {
                            words.forEach(word -> managementRepository().saveWord(word, modelSite));
                        }
                    }
                } else {
                    saveBadSite(modelSite);
                }
            }
        } else {
            ProjectManagement.STATUS = FixedValue.INDEXING_COMPLETE;
            Thread.currentThread().interrupt();
        }
    }

    private void saveBadSite(ModelSite modelSite) {
        managementRepository().saveBadSite(modelSite);
    }

    private void saveSystemSite(ModelSite modelSite){
        managementRepository().saveSystemSite(modelSite);
    }
}

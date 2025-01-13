package searchengine.searching.processing;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import searchengine.dto.entity.*;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.HashSet;
import java.util.List;

@Slf4j
public record DataIndexingEngine(AppManagementRepositoryImpl managementRepository) implements Runnable {

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
            if (FoundDataSite.checkUrl(url)) {
                saveSystemSite(url,modelSite);
            } else {
                Document document = FoundDataSite.getDocument(url);
                if (document != null) {
                    HashSet<String> links = FoundDataSite.getSiteLinks(document);
                    HashSet<String> words = FoundDataSite.getSiteWords(document);
                    String text = document.getAllElements().text();
                    if (text.isBlank() || text.length() < 5) {
                        saveSystemSite(url,modelSite);
                    } else {
                        managementRepository().saveStatistics(
                                modelSite.parentUrl(), words.size(), links.size(), ProjectManagement.STATUS);

                        if (!links.isEmpty()) {
                            List<FoundSiteEntity> foundSiteEntities = links.stream()
                                    .map(link ->
                                            FixedValue.getNewFoundSite(link, modelSite.parentUrl()))
                                    .toList();
                            managementRepository().saveFoundSites(foundSiteEntities);
                        }
                        if (url.toLowerCase().contains(modelSite.name().toLowerCase()) ||
                                url.toLowerCase().contains(modelSite.parentUrl().toLowerCase())) {
                            words.forEach(word -> managementRepository().saveWord(word, modelSite));
                        }
                    }
                } else {
                    saveBadSite(url, modelSite);
                }
            }
        } else {
            ProjectManagement.STATUS = FixedValue.INDEXING_COMPLETE;
            Thread.currentThread().interrupt();
        }
    }

    private void saveBadSite(String url, ModelSite modelSite) {
        managementRepository().saveBadSite(new BadSiteEntity(url.hashCode(), modelSite));
    }

    private void saveSystemSite(String url, ModelSite modelSite){
        managementRepository().saveSystemSite(
                new SystemSiteEntity(url.hashCode(), "Words not found", modelSite));
    }
}

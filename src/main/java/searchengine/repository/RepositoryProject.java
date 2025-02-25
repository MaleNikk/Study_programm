package searchengine.repository;

import searchengine.dto.entity.*;

import java.util.List;

public interface RepositoryProject {

    void saveSystemSite(ModelSite modelSite);

    void saveBadSite(ModelSite modelSite);

    void saveWords(String data);

    void saveFoundSites(List<ModelSite> foundSites);

    void saveParentSites(List<ModelParentSite> parentSites);

    void saveStatistics(String parentUrl, Integer lemmas, Integer sites, String status);

    void delete(String parentUrl);

    ModelParentSite takeParentSiteByUrl(String parentUrl);

    List<ModelSite> takeFoundSites();

    List<ModelWord> takeModelWords(String word, String url, int limit);

    List<ModelWord> showIndexedWords();

    List<ModelSite> showIndexedSites();

    List<ModelParentSite> takeParentSites();
}

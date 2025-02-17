package searchengine.searching.repository;

import searchengine.dto.entity.*;

import java.util.List;

public interface AppManagementRepositoryImpl {

    void saveSystemSite(ModelSite modelSite);

    void saveBadSite(ModelSite modelSite);

    void saveWord(List<ModelWord> modelWords);

    void saveFoundSites(List<ModelSite> foundSites);

    void saveParentSites(List<ModelParentSite> parentSites);

    void saveStatistics(String parentUrl, Integer lemmas, Integer sites, String status);

    void delete(String parentUrl);

    ModelSite getFoundSite();

    Integer countFoundSites();

    List<ModelWord> findModelWords(String word);

    List<ModelWord> showIndexedWords();

    List<ModelSite> showIndexedSites();

    List<ModelParentSite> getParentSites();
}

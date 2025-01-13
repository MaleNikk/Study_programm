package searchengine.searching.repository;

import searchengine.dto.entity.*;

import java.util.List;

public interface AppManagementRepositoryImpl {

    void saveSystemSite(SystemSiteEntity systemSiteEntity);

    void saveBadSite(BadSiteEntity badSiteEntity);

    void saveWord(String word,ModelSite modelSite);

    void saveFoundSites(List<FoundSiteEntity> foundSites);

    void saveParentSites(List<ParentSiteEntity> parentSites);

    void saveStatistics(String parentUrl, Integer lemmas, Integer sites, String status);

    void delete(String url);

    String getName(String parentUrl);

    ModelSite getFoundSite();

    Integer countFoundSites();

    List<ModelWord> findModelWords(String word,String parentUrl);

    List<ModelWord> showIndexedWords();

    List<ModelSite> showIndexedSites();

    List<ParentSiteEntity> getParentSites();

}

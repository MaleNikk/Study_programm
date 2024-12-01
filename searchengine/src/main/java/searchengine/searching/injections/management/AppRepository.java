package searchengine.searching.injections.management;

import searchengine.dto.entity.*;

import java.util.List;

public interface AppRepository {

    boolean checkWord(String word);

    boolean checkSite(String link);

    boolean checkSysSite(String link);

    boolean checkBadSite(String link);

    void saveWord(WordEntity searchEntity);

    void saveSite(SiteEntity siteEntity);

    void saveRegisteredSite(RegisteredSite registeredSite);

    void saveBadSite(BadSiteEntity badSiteEntity);

    void saveSysSite(SystemSiteEntity systemSiteEntity);

    WordEntity findByWord(String word);

    List<SiteEntity> showAllSites();

    List<RegisteredSite> showAllRegisteredSites();

    Integer getCountNewSite();

    Integer getCountRegisteredSites();

    Integer getCountBadSites();

    Integer getCountSysSites();

    List<WordEntity> showAllWords();

    void replaceEntity(SiteEntity siteEntity);

}

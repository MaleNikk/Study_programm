package searchengine.searching.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.*;
import searchengine.searching.injections.management.AppRepository;
import searchengine.searching.injections.storage.*;
import searchengine.searching.processing.ApplicationStatics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
@Data
public class ApplicationRepository implements AppRepository {

    @Autowired
    private AppRepositorySite repositorySite;

    @Autowired
    private AppRepositoryWord repositoryWord;

    @Autowired
    private AppSystemSiteRepository systemSiteRepository;

    @Autowired
    private AppBadSitesRepository badSitesRepository;

    @Autowired
    private AppRegisteredSiteRepository registeredSiteRepository;


    private final HashMap<String, SiteEntity> siteEntities = new HashMap<>();

    private final HashMap<String, RegisteredSite> registeredSiteEntities = new HashMap<>();

    private final HashMap<String, BadSiteEntity> badSiteEntities = new HashMap<>();

    private final HashMap<String, WordEntity> searchEntities = new HashMap<>();

    private final HashMap<String, SystemSiteEntity> systemSiteEntities = new HashMap<>();

    @Override
    public boolean checkWord(String word) {
        log.debug("Init method check word in data base. Repository class.");
        return !(getRepositoryWord().existsById(word) ||
                getSearchEntities().containsKey(word));
    }

    @Override
    public boolean checkSite(String path) {
        log.debug("Init method check site in data base. Repository class.");
        return (!getRepositorySite().existsById(path) &&
                !getSiteEntities().containsKey(path) &&
                !getRegisteredSiteRepository().existsById(path) &&
                !getRegisteredSiteEntities().containsKey(path));
    }

    @Override
    public boolean checkSysSite(String link) {
        log.debug("Init method check system site in data base. Repository class.");
        return !getSystemSiteEntities().containsKey(link) &&
                !getSystemSiteRepository().existsById(link);
    }

    @Override
    public boolean checkBadSite(String link) {
        log.debug("Init method check bad site in data base. Repository class.");
        return !getBadSitesRepository().existsById(link) &&
                !getBadSiteEntities().containsKey(link);
    }

    @Override
    public synchronized void saveWord(WordEntity wordEntity) {
        log.debug("Init method save word in data base. Repository class.");
        WordEntity checked = getRepositoryWord().findById(wordEntity.getWord()).orElse(null);
        if (checked != null) {
            checked.getSites().add(wordEntity.getSites().iterator().next());
            wordEntity.setSites(checked.getSites());
            getRepositoryWord().deleteById(wordEntity.getWord());
        }
        if (getSearchEntities().size() < ApplicationStatics.COUNT_WORDS) {
            getSearchEntities().put(wordEntity.getWord(), wordEntity);
        } else {
            getRepositoryWord().saveAll(getSearchEntities().values());
            getSearchEntities().clear();
            getSearchEntities().put(wordEntity.getWord(), wordEntity);
        }
    }

    @Override
    public synchronized void saveSite(SiteEntity siteEntity) {
        log.debug("Init method save site path in data base. Repository class.");
        if (checkSite(siteEntity.getUrl())) {
            if (getCountRegisteredSites() < ApplicationStatics.START_SAVE_SITE) {
                getRepositorySite().save(siteEntity);
            }
            if (getSiteEntities().size() < ApplicationStatics.COUNT_SITES) {
                getSiteEntities().put(siteEntity.getUrl(), siteEntity);
            } else {
                getRepositorySite().saveAll(getSiteEntities().values());
                getSiteEntities().clear();
                getSiteEntities().put(siteEntity.getUrl(), siteEntity);
            }
        }
    }

    @Override
    public synchronized void saveRegisteredSite(RegisteredSite registeredSite) {
        log.debug("Init method save registered site path in data base. Repository class.");
        if (checkRegisteredSite(registeredSite)) {
            if (getRegisteredSiteEntities().size() < ApplicationStatics.COUNT_SITES) {
                getRegisteredSiteEntities().put(registeredSite.getUrl(), registeredSite);
            } else {
                getRegisteredSiteRepository().saveAll(getRegisteredSiteEntities().values());
                getRegisteredSiteEntities().clear();
                getRegisteredSiteEntities().put(registeredSite.getUrl(), registeredSite);
            }
        }
    }

    @Override
    public synchronized void saveBadSite(BadSiteEntity badSiteEntity) {
        log.debug("Init method save bad site path in data base. Repository class.");
        if (checkBadSite(badSiteEntity.getUrl())) {
            if (getBadSiteEntities().size() < ApplicationStatics.COUNT_SITES) {
                getBadSiteEntities().put(badSiteEntity.getUrl(), badSiteEntity);
            } else {
                getBadSitesRepository().saveAll(getBadSiteEntities().values());
                getBadSiteEntities().clear();
                getBadSiteEntities().put(badSiteEntity.getUrl(), badSiteEntity);
            }
        }
    }


    @Override
    public synchronized void saveSysSite(SystemSiteEntity systemSiteEntity) {
        log.debug("Init method save system site path in data base. Repository class.");
        if (getSystemSiteEntities().size() < ApplicationStatics.COUNT_SITES) {
            getSystemSiteEntities().put(systemSiteEntity.getUrl(), systemSiteEntity);
        } else {
            getSystemSiteRepository().saveAll(getSystemSiteEntities().values());
            getSystemSiteEntities().clear();
            getSystemSiteEntities().put(systemSiteEntity.getUrl(), systemSiteEntity);
        }
    }

    @Override
    public WordEntity findByWord(String word) {
        log.debug("Init method find by word in data base. Repository class.");
        return getRepositoryWord().findById(word).orElse(
                new WordEntity(null, new HashSet<>(),  new HashSet<>()));
    }

    @Override
    public List<SiteEntity> showAllSites() {
        log.debug("Init method findAllSites from data base. Repository class.");
        return getRepositorySite().findAll();
    }

    @Override
    public List<RegisteredSite> showAllRegisteredSites() {
        log.debug("Init method findAllRegisteredSites from data base. Repository class.");
        return getRegisteredSiteRepository().findAll();
    }

    @Override
    public Integer getCountNewSite() {
        log.debug("Init method getCountNewSite from data base. Repository class.");
        return getRepositorySite().findAll().size();
    }

    @Override
    public Integer getCountRegisteredSites() {
        log.debug("Init method getCountSites from data base. Repository class.");
        return getRegisteredSiteRepository().findAll().size();
    }

    @Override
    public Integer getCountBadSites() {
        log.debug("Init method getCountBadSites from data base. Repository class.");
        return getBadSitesRepository().findAll().size();
    }


    @Override
    public Integer getCountSysSites() {
        log.debug("Init method getCountSysSites from data base. Repository class.");
        return getSystemSiteRepository().findAll().size();
    }

    @Override
    public List<WordEntity> showAllWords() {
        log.debug("Init method findAllSearch from data base. Repository class.");
        return getRepositoryWord().findAll();
    }

    @Override
    public void replaceEntity(SiteEntity siteEntity) {
        log.debug("Init method replace  entity from data base. Repository class.");
        getRepositorySite().delete(siteEntity);
    }

    private boolean checkRegisteredSite(RegisteredSite registeredSite) {
        return !getRegisteredSiteEntities().containsKey(registeredSite.getUrl()) ||
                !getRegisteredSiteRepository().existsById(registeredSite.getUrl());
    }
}

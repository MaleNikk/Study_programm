package searchengine.searching.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import searchengine.dto.entity.*;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.storage.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Data
@Slf4j
public class ManagementRepository implements AppManagementRepositoryImpl {

    @Autowired
    private NavigableRepository navigableRepository;

    @Autowired
    private BadSitesRepository badSitesRepository;

    @Autowired
    private SystemSiteRepository systemSiteRepository;

    @Autowired
    private WordsRepository wordsRepository;

    @Autowired
    private RepositoryAllSite repositoryAllSite;

    @Autowired
    private FoundSitesRepository foundSitesRepository;

    @Autowired
    private ParentSiteRepository parentSiteRepository;

    @Autowired
    private MongoOperations mongoOperations;

    private final ConcurrentHashMap<Integer, SystemSiteEntity> collectionSystemSites = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, BadSiteEntity> collectionBadSites = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, WordEntity> collectionWords = new ConcurrentHashMap<>();

    @Override
    public void saveSystemSite(SystemSiteEntity systemSiteEntity) {
        if (checkSystemSitesCollectionSize()) {
            getCollectionSystemSites().put(systemSiteEntity.getId(), systemSiteEntity);
        }
    }

    @Override
    public void saveBadSite(BadSiteEntity badSiteEntity) {
        if (checkBabSitesCollectionSize()) {
            getCollectionBadSites().put(badSiteEntity.getId(), badSiteEntity);
        }
    }

    @Override
    public void saveWord(String word, ModelSite modelSite) {
        Integer key = modelSite.parentUrl().hashCode();
        if (checkWordCollectionSize()) {
            saveNavigation(word);
            if (getWordsRepository().findById(word).isPresent()) {
                ModelWord saved = getWordsRepository().findById(word).get().getModelWord();
                updateWordEntity(word, modelSite, saved);
            } else if (getCollectionWords().containsKey(word)) {
                if (getCollectionWords().get(word).getModelWord().sites().containsKey(key)) {
                    getCollectionWords().get(word).getModelWord().sites().get(key).add(modelSite.url());
                } else {
                    getCollectionWords().get(word).getModelWord().sites().put(key, new HashSet<>(Set.of(modelSite.url())));
                }
            } else {
                getCollectionWords().put(word, FixedValue.getNewWordEntity(word,key,modelSite));
            }
        }
    }

    @Override
    public synchronized void saveFoundSites(List<FoundSiteEntity> foundSites) {
        List<FoundSiteEntity> forSave = new ArrayList<>();
        foundSites.forEach(foundSite -> {
            if (checkAllRepository(foundSite.getId())){
                forSave.add(foundSite);
            }});
        getFoundSitesRepository().saveAll(forSave);
    }

    @Override
    public void saveParentSites(List<ParentSiteEntity> parentSites) {
        parentSites.forEach(parentSite -> {
            if (!getParentSiteRepository().existsById(parentSite.getId())) {
                getParentSiteRepository().save(parentSite);
            }
        });
    }

    @Override
    public void saveStatistics(String parentUrl, Integer lemmas, Integer pages, String status) {
        if (getParentSiteRepository().existsById(parentUrl.hashCode())) {
            updateStatistics(parentUrl, lemmas, pages, status);
        }
    }

    @Override
    public String getName(String parentUrl) {
        if (getParentSiteRepository().findById(parentUrl.hashCode()).isPresent()) {
            return getParentSiteRepository().findById(parentUrl.hashCode()).get().getModelParentSite().getName();
        } else {
            return FixedValue.SEARCH_IN_ALL;
        }
    }

    @Override
    public synchronized ModelSite getFoundSite() {
        ModelSite modelSite = null;
        if (getFoundSitesRepository().findAll().stream().findAny().isPresent()) {
            modelSite = getFoundSitesRepository().findAll().stream().findAny().get().getModelSite();
        }
        if (modelSite != null) {
            getRepositoryAllSite().save(new AllSitesEntity(modelSite.url().hashCode(), modelSite));
            getFoundSitesRepository().deleteById(modelSite.url().hashCode());
        }
        return modelSite;
    }

    @Override
    public synchronized Integer countFoundSites() {
        if (getFoundSitesRepository().findAll().isEmpty()) {
            return FixedValue.ZERO;
        } else {
            return getFoundSitesRepository().findAll().size();
        }
    }

    @Override
    public Integer countIndexedSites() {
        return getRepositoryAllSite().findAll().size();
    }

    public List<ModelWord> findModelWords(String word, String parentUrl) {
        String id = getId(word);
        Integer searchKey = parentUrl.hashCode();
        log.info("Init get collection words from repository.");
        List<ModelWord> modelWords = new ArrayList<>();
        if (getNavigableRepository().findById(id).isPresent()) {
            getNavigableRepository().findById(id).get().getWords().forEach(key -> {
                if (getWordsRepository().findById(key).isPresent()) {
                    ModelWord modelWord = getWordsRepository().findById(key).get().getModelWord();
                    if (Objects.equals(parentUrl, FixedValue.SEARCH_IN_ALL)) {
                        modelWords.add(modelWord);
                    } else {
                        if (modelWord.sites().containsKey(searchKey)) {
                            modelWords.add(modelWord);
                        }
                    }
                }
            });
        }
        return modelWords;
    }

    @Override
    public List<ModelWord> showIndexedWords() {
        return getWordsRepository().findAll().subList(0,250).stream().map(WordEntity::getModelWord).toList();
    }

    @Override
    public List<ModelSite> showIndexedSites() {
        return getRepositoryAllSite().findAll().subList(0,50).stream().map(AllSitesEntity::getModelSite).toList();
    }

    @Override
    public List<ParentSiteEntity> getParentSites() {
        return getParentSiteRepository().findAll();
    }

    @Override
    public void delete(){
        getRepositoryAllSite().deleteAll();
        getFoundSitesRepository().deleteAll();
    }

    private void saveWordsEntity() {
        getWordsRepository().saveAll(getCollectionWords().values());
        getCollectionWords().clear();
    }

    private void saveSystemSites() {
        getSystemSiteRepository().saveAll(getCollectionSystemSites().values());
        getCollectionSystemSites().clear();
    }

    private void saveBadSites() {
        getBadSitesRepository().saveAll(getCollectionBadSites().values());
        getCollectionBadSites().clear();
    }

    private synchronized boolean checkSystemSitesCollectionSize() {
        if (getCollectionSystemSites().size() > FixedValue.COUNT_SITES) {
            saveSystemSites();
        }
        return FixedValue.TRUE;
    }

    private synchronized boolean checkBabSitesCollectionSize() {
        if (getCollectionBadSites().size() > FixedValue.COUNT_SITES) {
            saveBadSites();
        }
        return FixedValue.TRUE;
    }

    private synchronized boolean checkWordCollectionSize() {
        if (getCollectionWords().size() > FixedValue.COUNT_SITES) {
            saveWordsEntity();
        }
        return FixedValue.TRUE;
    }
    private synchronized boolean checkParentSite(Integer id){
        return getParentSiteRepository().findById(id).isPresent();
    }
    private boolean checkAllRepository(Integer id){
        if (getRepositoryAllSite().findAll().isEmpty()){
            return FixedValue.TRUE;
        }
        return !getRepositoryAllSite().existsById(id);
    }

    private void updateNavigableEntity(String word) {
        String id = getId(word);
        if (getNavigableRepository().findById(id).isPresent()) {
            HashSet<String> updated = getNavigableRepository().findById(id).get().getWords();
            updated.add(word);
            getMongoOperations().updateFirst(query(where("id").is(id)),
                    update("words", updated), NavigableEntity.class);
        }
    }

    private void updateWordEntity(String word, ModelSite modelSite, ModelWord saved) {
        Integer key = modelSite.parentUrl().hashCode();
        if (saved.sites().containsKey(key)) {
            saved.sites().get(key).add(modelSite.url());
        } else {
            saved.sites().put(key, new HashSet<>(Set.of(modelSite.url())));
        }
        getMongoOperations().updateFirst(query(where("id").is(word)), update("modelWord", saved), WordEntity.class);
    }

    private void updateStatistics(String url, Integer lemmas, Integer pages, String status) {
        Integer id = url.hashCode();
        if (checkParentSite(id)) {
            ModelParentSite saved = getParentSiteRepository().findById(id).get().getModelParentSite();
                    saved.setStatus(status);
                    saved.setStatusTime(System.nanoTime());
                    saved.setPages(saved.getPages() + pages);
                    saved.setLemmas(saved.getLemmas() + lemmas);
            getMongoOperations().updateFirst(query(where("id").is(id)),
                    update("modelParentSite", saved), ParentSiteEntity.class);
        }
    }

    private void saveNavigation(String word) {
        String id = getId(word);
        if (!getNavigableRepository().existsById(id)) {
            getNavigableRepository().save(new NavigableEntity(id, new HashSet<>(Set.of(word))));
        } else {
            updateNavigableEntity(word);
        }
    }

    private String getId(String word) {
        return word.length() <= 5 ? word.substring(0, 2) : word.substring(0, 4);
    }
}

package searchengine.searching.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.*;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.storage.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
@Slf4j
public final class ManagementRepository implements AppManagementRepositoryImpl {

    private final NavigableRepository navigableRepository;

    private final BadSitesRepository badSitesRepository;

    private final SystemSiteRepository systemSiteRepository;

    private final WordsRepository wordsRepository;

    private final RepositoryAllSite repositoryAllSite;

    private final FoundSitesRepository foundSitesRepository;

    private final ParentSiteRepository parentSiteRepository;

    private final MongoOperations mongoOperations;

    private final ConcurrentHashMap<Integer, SystemSiteEntity> collectionSystemSites = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, BadSiteEntity> collectionBadSites = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, WordEntity> collectionWords = new ConcurrentHashMap<>();

    @Autowired
    public ManagementRepository(
            NavigableRepository navigableRepository,
            BadSitesRepository badSitesRepository,
            SystemSiteRepository systemSiteRepository,
            WordsRepository wordsRepository,
            RepositoryAllSite repositoryAllSite,
            FoundSitesRepository foundSitesRepository,
            ParentSiteRepository parentSiteRepository,
            MongoOperations mongoOperations) {
        this.navigableRepository = navigableRepository;
        this.badSitesRepository = badSitesRepository;
        this.systemSiteRepository = systemSiteRepository;
        this.wordsRepository = wordsRepository;
        this.repositoryAllSite = repositoryAllSite;
        this.foundSitesRepository = foundSitesRepository;
        this.parentSiteRepository = parentSiteRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void saveSystemSite(SystemSiteEntity systemSiteEntity) {
        if (checkSystemSitesCollectionSize()) {
            collectionSystemSites.put(systemSiteEntity.getId(), systemSiteEntity);
        }
    }

    @Override
    public void saveBadSite(BadSiteEntity badSiteEntity) {
        if (checkBabSitesCollectionSize()) {
            collectionBadSites.put(badSiteEntity.getId(), badSiteEntity);
        }
    }

    @Override
    public void saveWord(String word, ModelSite modelSite) {
        Integer key = modelSite.parentUrl().hashCode();
        if (checkWordCollectionSize()) {
            saveNavigation(word);
            if (wordsRepository.findById(word).isPresent()) {
                ModelWord saved = wordsRepository.findById(word).get().getModelWord();
                updateWordEntity(word, modelSite, saved);
            } else if (collectionWords.containsKey(word)) {
                if (collectionWords.get(word).getModelWord().sites().containsKey(key)) {
                    collectionWords.get(word).getModelWord().sites().get(key).add(modelSite.url());
                } else {
                    collectionWords.get(word).getModelWord().sites().put(key, new HashSet<>(Set.of(modelSite.url())));
                }
            } else {
                collectionWords.put(word, FixedValue.getNewWordEntity(word, key, modelSite));
            }
        }
    }

    @Override
    public synchronized void saveFoundSites(List<FoundSiteEntity> foundSites) {
        List<FoundSiteEntity> forSave = foundSites.stream()
                .filter(site -> checkAllRepository(site.getId())).toList();
        if (!forSave.isEmpty()) {
            foundSitesRepository.saveAll(forSave);
        }
    }

    @Override
    public void saveParentSites(List<ParentSiteEntity> parentSites) {
        parentSites.forEach(parentSite -> {
            if (!parentSiteRepository.existsById(parentSite.getId())) {
                parentSiteRepository.save(parentSite);
            }
        });
    }

    @Override
    public void saveStatistics(String parentUrl, Integer lemmas, Integer pages, String status) {
        if (parentSiteRepository.existsById(parentUrl.hashCode())) {
            updateStatistics(parentUrl, lemmas, pages, status);
        }
    }

    @Override
    public String getName(String parentUrl) {
        if (parentSiteRepository.findById(parentUrl.hashCode()).isPresent()) {
            return parentSiteRepository.findById(parentUrl.hashCode()).get().getModelParentSite().getName();
        } else {
            return FixedValue.SEARCH_IN_ALL;
        }
    }

    @Override
    public synchronized ModelSite getFoundSite() {
        ModelSite modelSite = null;
        if (foundSitesRepository.findAll().stream().findAny().isPresent()) {
            modelSite = foundSitesRepository.findAll().stream().findAny().get().getModelSite();
        }
        if (modelSite != null) {
            repositoryAllSite.save(new AllSitesEntity(modelSite.url().hashCode(), modelSite));
            foundSitesRepository.deleteById(modelSite.url().hashCode());
        }
        return modelSite;
    }

    @Override
    public synchronized Integer countFoundSites() {
        if (foundSitesRepository.findAll().isEmpty()) {
            return FixedValue.ZERO;
        } else {
            return foundSitesRepository.findAll().size();
        }
    }

    public List<ModelWord> findModelWords(String word, String parentUrl) {
        String id = getId(word);
        Integer searchKey = parentUrl.hashCode();
        log.info("Init get collection words from repository.");
        List<ModelWord> modelWords = new ArrayList<>();
        if (navigableRepository.findById(id).isPresent()) {
            navigableRepository.findById(id).get().getWords().forEach(key -> {
                if (wordsRepository.findById(key).isPresent()) {
                    ModelWord modelWord = wordsRepository.findById(key).get().getModelWord();
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
        return wordsRepository.findAll().subList(0, 250).stream().map(WordEntity::getModelWord).toList();
    }

    @Override
    public List<ModelSite> showIndexedSites() {
        return repositoryAllSite.findAll().subList(0, 50).stream().map(AllSitesEntity::getModelSite).toList();
    }

    @Override
    public List<ParentSiteEntity> getParentSites() {
        return parentSiteRepository.findAll();
    }

    @Override
    public void delete(String url) {
        if (Objects.equals(url, FixedValue.SEARCH_IN_ALL)) {
            repositoryAllSite.deleteAll();
            foundSitesRepository.deleteAll();
        } else {
            repositoryAllSite.deleteById(url.hashCode());
            badSitesRepository.deleteById(url.hashCode());
            foundSitesRepository.deleteById(url.hashCode());
        }
    }

    private void saveWordsEntity() {
        wordsRepository.saveAll(collectionWords.values());
        collectionWords.clear();
    }

    private void saveSystemSites() {
        systemSiteRepository.saveAll(collectionSystemSites.values());
        collectionSystemSites.clear();
    }

    private void saveBadSites() {
        badSitesRepository.saveAll(collectionBadSites.values());
        collectionBadSites.clear();
    }

    private synchronized boolean checkSystemSitesCollectionSize() {
        if (collectionSystemSites.size() > FixedValue.COUNT_SITES) {
            saveSystemSites();
        }
        return FixedValue.TRUE;
    }

    private synchronized boolean checkBabSitesCollectionSize() {
        if (collectionBadSites.size() > FixedValue.COUNT_SITES) {
            saveBadSites();
        }
        return FixedValue.TRUE;
    }

    private synchronized boolean checkWordCollectionSize() {
        if (collectionWords.size() > FixedValue.COUNT_SITES) {
            saveWordsEntity();
        }
        return FixedValue.TRUE;
    }

    private synchronized boolean checkParentSite(Integer id) {
        return parentSiteRepository.findById(id).isPresent();
    }

    private boolean checkAllRepository(Integer id) {
        if (repositoryAllSite.findAll().isEmpty() && foundSitesRepository.findAll().isEmpty()) {
            return FixedValue.TRUE;
        }
        return !repositoryAllSite.existsById(id) && !foundSitesRepository.existsById(id);
    }

    private void updateNavigableEntity(String word) {
        String id = getId(word);
        if (navigableRepository.findById(id).isPresent()) {
            HashSet<String> updated = navigableRepository.findById(id).get().getWords();
            updated.add(word);
            mongoOperations.updateFirst(query(where("id").is(id)),
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
        mongoOperations.updateFirst(query(where("id").is(word)), update("modelWord", saved), WordEntity.class);
    }

    private void updateStatistics(String url, Integer lemmas, Integer pages, String status) {
        Integer id = url.hashCode();
        if (checkParentSite(id)) {
            ModelParentSite saved = parentSiteRepository.findById(id).get().getModelParentSite();
            saved.setStatus(status);
            saved.setStatusTime(System.nanoTime());
            saved.setPages(saved.getPages() + pages);
            saved.setLemmas(saved.getLemmas() + lemmas);
            mongoOperations.updateFirst(query(where("id").is(id)),
                    update("modelParentSite", saved), ParentSiteEntity.class);
        }
    }

    private void saveNavigation(String word) {
        String id = getId(word);
        if (!navigableRepository.existsById(id)) {
            navigableRepository.save(new NavigableEntity(id, new HashSet<>(Set.of(word))));
        } else {
            updateNavigableEntity(word);
        }
    }

    private String getId(String word) {
        return word.length() <= 5 ? word.substring(0, 2) : word.substring(0, 4);
    }
}

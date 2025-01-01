package searchengine.searching.processing;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.entity.WordEntity;
import searchengine.dto.model.ModelAnswerResponse;
import searchengine.dto.model.ModelSearch;
import searchengine.dto.model.ModelSiteResponse;
import searchengine.searching.injections.logic.AppSearching;
import searchengine.searching.injections.management.AppRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Data
public class ApplicationSearchingWords implements AppSearching {

    @Autowired
    private AppRepository repository;

    private volatile Boolean start = false;

    private Double relevance;

    private Integer countWords = 0;

    private AtomicInteger countPools;

    private List<ModelSiteResponse> sites = new ArrayList<>();

    private ModelAnswerResponse modelAnswerResponse = new ModelAnswerResponse(false, 0 , new HashSet<>());

    private AtomicLong timeStart = new AtomicLong(System.currentTimeMillis());

    private List<Boolean> treadsLife = new ArrayList<>();

    public void startSearch(ModelSearch modelSearch, Integer index, Integer countThreads) {
        setStart(true);
        AtomicInteger indexWord = new AtomicInteger(ApplicationStatics.APP_ZERO);
        getRepository().showAllWords().forEach(wordEntity -> {
            if (getStart()) {
                if (getSites().size() < modelSearch.getLimit()) {
                    int indexEntity = indexWord.incrementAndGet();
                    if (indexEntity == index) {
                        if (wordEntity.getParentsUrl().contains(modelSearch.getParentSite())) {
                            searchingWord(modelSearch, wordEntity);
                        }
                        if (modelSearch.getParentSite().equals("all")) {
                            searchingWord(modelSearch, wordEntity);
                        }
                    }
                    if (indexEntity == countThreads) {
                        indexWord.set(ApplicationStatics.APP_ZERO);
                    }
                } else {
                    stopSearch();
                }
            } else {
                stopSearch();
            }
        });
    }


    @Override
    @SneakyThrows
    public ModelAnswerResponse getModelAnswer(ModelSearch modelSearch) {

        log.info("Init searching engine!");

        long timeStart = System.currentTimeMillis();
        getSites().clear();
        setCountPools(new AtomicInteger(ApplicationStatics.COUNT_THREADS));
        findWordById(modelSearch);
        if (!getRepository().showAllWords().isEmpty()) {
            do {
                int index = getCountPools().get();
                Thread thread = new Thread(() -> startSearch(modelSearch, index, ApplicationStatics.COUNT_THREADS));
                thread.start();
                log.info("Start thread for searching: {}", thread.getName());
                getCountPools().getAndDecrement();
            } while (getCountPools().get() > ApplicationStatics.APP_ZERO);

            do  {
                log.info("Searching in progress! Time searching: {} sec.",
                        (System.currentTimeMillis() - timeStart)/1000);
                Thread.sleep(ApplicationStatics.TIME_SLEEP);
            } while (getTreadsLife().size() < ApplicationStatics.COUNT_THREADS);
            getTreadsLife().clear();

            log.info("Searching complete!");

            getModelAnswerResponse().setCount(getCountWords());
            getModelAnswerResponse().getData().addAll(getSites());
            getSites().clear();
        }
        return getModelAnswerResponse();
    }

    private boolean checkChars(String searchWord, String entityWord) {
        return entityWord.contains(searchWord.substring(0, 2));
    }

    private void addDataToSet(RegisteredSite registeredSite, String word) {
        ModelSiteResponse modelSite = ModelSiteResponse.from(registeredSite, word, getRelevance());
        getSites().add(modelSite);
    }

    private void searchingWord(ModelSearch modelSearch, WordEntity wordEntity) {
        if (checkChars(modelSearch.getWord(), wordEntity.getWord())) {
            List<Boolean> checkSearch = new ArrayList<>();
            char[] entityChars = wordEntity.getWord().toCharArray();
            char[] wordChars = modelSearch.getWord().toCharArray();
            int minCountChars = Math.min(entityChars.length, wordChars.length);
            int maxCountChars = Math.max(entityChars.length, wordChars.length);
            for (int a = ApplicationStatics.APP_ZERO; a <= maxCountChars - minCountChars; a++) {
                for (int i = ApplicationStatics.APP_ZERO; i < minCountChars; i++) {
                    if (wordChars.length <= entityChars.length) {
                        if (Objects.equals(wordChars[i],entityChars[i + a])) {
                            checkSearch.add(ApplicationStatics.TRUE);
                        }
                    } else {
                        if (Objects.equals(wordChars[i+a],entityChars[i])) {
                            checkSearch.add(ApplicationStatics.TRUE);
                        }
                    }
                }

                if (checkSearch.size() > (wordChars.length - ApplicationStatics.INDEX_PRESENT) ||
                        checkSearch.size() == (wordChars.length - ApplicationStatics.INDEX_PRESENT)) {
                    getModelAnswerResponse().setResult(ApplicationStatics.TRUE);
                    setRelevance((double) (minCountChars / maxCountChars));
                    setCountWords(getCountWords()+1);
                    wordEntity.getSites().forEach(registeredSite -> {
                        getModelAnswerResponse()
                                .setCount(getModelAnswerResponse()
                                        .getCount() + registeredSite.getCountWords());
                    });
                    wordEntity.getSites().forEach(registeredSite -> addDataToSet(registeredSite, wordEntity.getWord()));
                }
                checkSearch.clear();
            }
        }
    }

    private void findWordById(ModelSearch modelSearch){
        if (!getRepository().checkWord(modelSearch.getWord())){
            WordEntity wordEntity = getRepository().findByWord(modelSearch.getWord());
            wordEntity.getSites().forEach(registeredSite -> addDataToSet(registeredSite, wordEntity.getWord()));
        }
    }

    private void stopSearch(){
        getTreadsLife().add(true);
        Thread.currentThread().interrupt();
    }
}

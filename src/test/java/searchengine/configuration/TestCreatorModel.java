package searchengine.configuration;

import searchengine.dto.entity.ModelParentSite;
import searchengine.dto.entity.ModelWord;
import searchengine.dto.model.ModelFinder;
import searchengine.config.FixedValue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestCreatorModel {

    public static final ModelFinder TEST_MODEL_SEARCH = initModel();

    public static final ExecutorService TEST_EXECUTOR = Executors.newFixedThreadPool(5);

    public static final ConcurrentLinkedQueue<List<ModelWord>> LINKED_QUEUE = initCollectionModelWords();

    public static final List<ModelParentSite> PARENT_SITE_LIST_SAVE = initCollectionParentSites(FixedValue.ZERO);

    public static final List<ModelParentSite> PARENT_SITE_LIST_UPDATE = initCollectionParentSites(FixedValue.TIME_SLEEP);

    private static ModelFinder initModel(){
        return new ModelFinder("test_1","http://localhost:8080/test_1", FixedValue.ZERO,FixedValue.ZERO);
    }

    private static ConcurrentLinkedQueue<List<ModelWord>> initCollectionModelWords() {
        ConcurrentLinkedQueue<List<ModelWord>> linkedQueue = new ConcurrentLinkedQueue<>();
        String[] objectData = {"test","testing","http://localhost:8080/test","http://localhost","name"};
        AtomicInteger countTest = new AtomicInteger(FixedValue.ZERO);
        do {
            List<ModelWord> modelWords = new ArrayList<>();
            do {
                modelWords.add(new ModelWord(
                        initNextData(countTest, objectData[0]),
                        initNextData(countTest, objectData[2]),
                        initNextData(countTest, objectData[3]),
                        initNextData(countTest, objectData[4]),
                        countTest.get()
                ));
            } while (modelWords.size() < 4);
            linkedQueue.add(modelWords);
        } while (linkedQueue.size() < 100);
        return linkedQueue;
    }

    private static List<ModelParentSite> initCollectionParentSites(int digitTest) {
        AtomicInteger countTest = new AtomicInteger(FixedValue.ZERO);
        String[] objectData = {"test_url", "test_name", String.valueOf(Instant.now()), "TEST", FixedValue.NO_ERROR};
        List<ModelParentSite> parentSites = new ArrayList<>();
        do {
            parentSites.add(new ModelParentSite(
                    initNextData(countTest, objectData[0]),
                    initNextData(countTest, objectData[1]),
                    initNextData(countTest, objectData[2]),
                    initNextData(countTest, objectData[3]),
                    System.currentTimeMillis(),
                    initNextData(countTest, objectData[4]),
                    FixedValue.ZERO + digitTest,
                    FixedValue.ZERO + digitTest
            ));
        } while (parentSites.size() < 100);
        return parentSites;
    }

    private static String initNextData(AtomicInteger countTest, String data) {
        return data.concat("_").concat(String.valueOf(countTest.getAndIncrement()));
    }
}

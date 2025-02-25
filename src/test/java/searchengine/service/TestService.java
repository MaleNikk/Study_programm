package searchengine.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.configuration.TestConfiguration;
import searchengine.dto.model.ModelFinder;
import searchengine.configuration.TestCreatorModel;

public final class TestService extends TestConfiguration {

    @Test
    @DisplayName("Test init start method")
    public void whenInitStartMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.startIndexing(), serviceBuildingData.start());
        System.out.println("\nTest start indexing complete successful.");
    }

    @Test
    @DisplayName("Test init stop method")
    public void whenInitStopMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.stopIndexing(), serviceBuildingData.stop());
        System.out.println("\nTest stop indexing complete successful.");
    }

    @Test
    @DisplayName("Test init statistics method")
    public void whenInitStatisticsMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.getStatistics(),management.getStatistics());
        System.out.println("\nTest update statistics complete successful.");
    }

    @Test
    @DisplayName("Test init search method")
    public void whenInitSearchMethod_thenReturnNeededClassMethod(){
        ModelFinder modelFinder = TestCreatorModel.TEST_MODEL_SEARCH;
        Assertions.assertEquals(service.findByWord(modelFinder), serviceFindingData.findByWord(modelFinder));
        System.out.println("\nTest search method complete successful.");
    }
}

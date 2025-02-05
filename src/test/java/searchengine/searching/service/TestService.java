package searchengine.searching.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import searchengine.dto.model.ModelSearch;
import searchengine.searching.processing.indexing.IndexingManager;
import searchengine.searching.processing.managing.AppManagementImpl;
import searchengine.searching.processing.searching.SearchingManager;
import searchengine.temporary.TestCreatorModel;

public final class TestService {

    AppServiceImpl service = Mockito.mock(AppServiceImpl.class);

    IndexingManager indexingManager = Mockito.mock(IndexingManager.class);

    SearchingManager searchingManager = Mockito.mock(SearchingManager.class);

    AppManagementImpl management = Mockito.mock(AppManagementImpl.class);

    @Test
    @DisplayName("Test init start method")
    public void whenInitStartMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.startIndexing(),indexingManager.start());
    }

    @Test
    @DisplayName("Test init stop method")
    public void whenInitStopMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.stopIndexing(),indexingManager.stop());
    }

    @Test
    @DisplayName("Test init statistics method")
    public void whenInitStatisticsMethod_thenReturnNeededClassMethod(){
        Assertions.assertEquals(service.getStatistics(),management.getStatistics());
    }

    @Test
    @DisplayName("Test init search method")
    public void whenInitSearchMethod_thenReturnNeededClassMethod(){
        ModelSearch modelSearch = TestCreatorModel.TEST_MODEL_SEARCH;
        Assertions.assertEquals(service.findByWord(modelSearch),searchingManager.findByWord(modelSearch));
    }
}

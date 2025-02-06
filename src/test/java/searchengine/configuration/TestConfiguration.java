package searchengine.configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import searchengine.searching.processing.indexing.IndexingManager;
import searchengine.searching.processing.managing.AppManagementImpl;
import searchengine.searching.processing.searching.SearchingManager;
import searchengine.searching.repository.AppManagementRepositoryImpl;
import searchengine.searching.service.AppServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestConfiguration {

    @MockitoBean
    public AppServiceImpl service;

    @MockitoBean
    public AppManagementImpl management;

    @MockitoBean
    public AppManagementRepositoryImpl repository;

    @MockitoBean
    public SearchingManager searchingManager;

    @MockitoBean
    public IndexingManager indexingManager;

    @LocalServerPort
    public Integer serverPort;

}

package searchengine.configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import searchengine.service.ServiceBuildingData;
import searchengine.service.ServiceAdditions;
import searchengine.service.ServiceFindingData;
import searchengine.repository.RepositoryProject;
import searchengine.service.ServiceApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestConfiguration {

    @MockitoBean
    public ServiceApplication service;

    @MockitoBean
    public ServiceAdditions management;

    @MockitoBean
    public RepositoryProject repository;

    @MockitoBean
    public ServiceFindingData serviceFindingData;

    @MockitoBean
    public ServiceBuildingData serviceBuildingData;

    @LocalServerPort
    public Integer serverPort;

}

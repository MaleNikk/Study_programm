package searchengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import searchengine.searching.repository.AppManagementRepositoryImpl;
import searchengine.searching.repository.ManagementRepository;
import searchengine.searching.service.AppServiceImpl;
import searchengine.searching.service.ProjectService;

@Configuration
public class ApplicationConfig {

    @Bean
    @Scope(value = "singleton")
    public AppManagementRepositoryImpl managementRepository(){
        return new ManagementRepository();
    }

    @Bean
    @Scope(value = "singleton")
    public AppServiceImpl appService(){
        return new ProjectService();
    }
}

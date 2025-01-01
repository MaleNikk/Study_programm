package searchengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import searchengine.searching.repository.AppManagementRepositoryImpl;
import searchengine.searching.repository.ManagementRepository;

@Configuration
public class ApplicationConfig {

    @Bean
    @Scope(value = "singleton")
    public AppManagementRepositoryImpl managementRepository(){
        return new ManagementRepository();
    }
}

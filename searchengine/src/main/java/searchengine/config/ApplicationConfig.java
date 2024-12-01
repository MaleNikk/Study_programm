package searchengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.logic.AppSearching;
import searchengine.searching.injections.logic.AppWorker;
import searchengine.searching.injections.management.AppManagement;
import searchengine.searching.injections.management.AppStatistics;
import searchengine.searching.processing.*;

@Configuration
public class ApplicationConfig {

    @Bean
    @Scope(value = "singleton")
    public AppLogic appLogic() {
        return new ApplicationLogicClass();
    }

    @Bean
    @Scope(value = "singleton")
    public AppManagement appManagement() {
        return new ApplicationManagement();
    }

    @Bean
    @Scope(value = "singleton")
    public AppWorker appWorker() {
        return new ApplicationWorker();
    }

    @Bean
    @Scope(value = "singleton")
    public AppSearching appSearching(){
        return new ApplicationSearchingWords();
    }

    @Bean
    @Scope(value = "singleton")
    public AppStatistics appStatistics() { return new ApplicationStatisticBuilder();}
}

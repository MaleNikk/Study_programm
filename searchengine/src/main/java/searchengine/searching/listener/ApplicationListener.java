package searchengine.searching.listener;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import searchengine.config.SitesList;
import searchengine.dto.entity.BasicSiteEntity;
import searchengine.searching.injections.management.AppStatistics;
import searchengine.searching.injections.storage.AppBasicSiteRepository;

import java.time.LocalDateTime;

@Component
@Data
public class ApplicationListener {

    @Autowired
    private SitesList sitesList;

    @Autowired
    private AppStatistics appStatistics;

    @Autowired
    private AppBasicSiteRepository basicSiteRepository;

    @EventListener(ApplicationStartedEvent.class)
    private void initDataBase(){
       getSitesList()
               .getSites()
               .forEach(site -> getBasicSiteRepository()
                       .save(new BasicSiteEntity(site.getUrl(),site.getName(), LocalDateTime.now())));
       getAppStatistics().initStatistics();
    }
}

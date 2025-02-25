package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.ModelParentSite;
import searchengine.repository.RepositoryProject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "indexing-settings")
public class SitesList {

    private List<Site> sites;

    @Autowired
    private RepositoryProject repository;

    @EventListener(ApplicationStartedEvent.class)
    private void initParentSites() {
        if (!sites.isEmpty()) {
            List<ModelParentSite> parentSiteEntities = new ArrayList<>();
            sites.forEach(site -> {
                ModelParentSite parentSite = FixedValue.getNewModelParentSite(site.getUrl(), site.getName());
                parentSiteEntities.add(parentSite);
            });
            repository.saveParentSites(parentSiteEntities);
        }
    }
}

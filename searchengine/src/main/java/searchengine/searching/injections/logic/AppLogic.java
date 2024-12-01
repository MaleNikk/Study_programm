package searchengine.searching.injections.logic;

import searchengine.dto.entity.SiteEntity;

public interface AppLogic {

    void buildDataSites(SiteEntity siteEntity);

    void stop(boolean command);

    boolean getStop();

    void setStatus(String status);

    String getStatus();
}

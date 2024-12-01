package searchengine.searching.processing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.entity.RegisteredSite;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.management.AppStatistics;
import searchengine.searching.injections.storage.AppBasicSiteRepository;

import java.util.HashMap;

@Slf4j
@Data
public class ApplicationStatisticBuilder implements AppStatistics {

    @Autowired
    private AppLogic appLogic;

    @Autowired
    private AppBasicSiteRepository basicSiteRepository;

    private HashMap<String, DetailedStatisticsItem> itemHashMap = new HashMap<>();

    private TotalStatistics totalStatistics = new TotalStatistics(0,0,0,true);

    @Override
    public void initStatistics(){
        log.info("Init statistics builder!");
       getBasicSiteRepository().findAll().forEach(basicSiteEntity ->
                getItemHashMap().put(basicSiteEntity.getUrl(),
                        new DetailedStatisticsItem(
                                basicSiteEntity.getUrl(),
                                basicSiteEntity.getName(),
                                "",
                                0L,
                                "We try to work without errors!",
                                0,
                                0)));
    }

    @Override
    public void buildStatistics(RegisteredSite registeredSite) {
     DetailedStatisticsItem detailedStatisticsItem =  getItemHashMap().get(registeredSite.getParentUrl());
     detailedStatisticsItem.setStatusTime(System.currentTimeMillis());
     detailedStatisticsItem.setLemmas(detailedStatisticsItem.getLemmas() + registeredSite.getCountWords());
     detailedStatisticsItem.setPages(detailedStatisticsItem.getPages() + registeredSite.getPages());
     detailedStatisticsItem.setStatus(getAppLogic().getStatus());
     getTotalStatistics().setSites(getBasicSiteRepository().findAll().size());
     getTotalStatistics().setLemmas(getTotalStatistics().getLemmas() + registeredSite.getCountWords());
     getTotalStatistics().setPages(getTotalStatistics().getPages() + registeredSite.getPages());
     getTotalStatistics().setIndexing(getAppLogic().getStop());
    }

    @Override
    public StatisticsResponse getStatistics(){
        return
                new StatisticsResponse(
                ApplicationStatics.TRUE,
                new StatisticsData(
                        getTotalStatistics(),
                        getItemHashMap().values().stream().toList()));
    }
}

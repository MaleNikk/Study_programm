package searchengine.searching.processing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.entity.*;
import searchengine.searching.injections.logic.AppLogic;
import searchengine.searching.injections.management.AppRepository;
import searchengine.searching.injections.management.AppStatistics;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Data
public class ApplicationLogicClass implements AppLogic {

    @Autowired
    private AppRepository repository;

    @Autowired
    private AppStatistics appStatistics;

    private volatile boolean stop = false;

    private String status;

    @Override
    public void stop(boolean command) {
        log.info("Stop loading data to data base. ApplicationLogic class.");
        setStop(command);
    }

    @Override
    public boolean getStop() {
        return stop;
    }

    @Override
    public void buildDataSites(SiteEntity siteEntity) {
        if (ApplicationConnection.checkSite(siteEntity.getUrl())) {
            getRepository().replaceEntity(siteEntity);
            if (getStop()) {
                log.debug("Start loading data from site to data base. ApplicationLogic class.");
                String startPath = siteEntity.getUrl();
                String incomeData = ApplicationConnection.getDataFromSite(startPath);
                String dataIncome = ApplicationConnection.getTextFromSite(startPath);
                String[] pages = incomeData.split(ApplicationStatics.REGEX_HTTP);
                if (checkSystemFile(incomeData, siteEntity.getUrl(), dataIncome)) {
                    String[] income = dataIncome.split(" ");
                    HashSet<String> lemmas = new HashSet<>(Arrays.stream(income).toList());
                    RegisteredSite registeredSite =
                            new RegisteredSite(
                                    siteEntity.getUrl(),
                                    siteEntity.getParentUrl(),
                                    siteEntity.getName(),
                                    lemmas.size(),
                                    pages.length - 1
                            );
                    getRepository().saveRegisteredSite(registeredSite);
                    getAppStatistics().buildStatistics(registeredSite);
                    buildDataWords(siteEntity, lemmas, registeredSite);
                    for (String data : pages) {
                        if (data.length() > 5 && data.substring(0, 4).contains(ApplicationStatics.REGEX_CHECK_LINK)) {
                            String[] secondData = data.
                                    replaceAll(ApplicationStatics.REGEX_POINT, "!").
                                    split("!");
                            String sitePath = ApplicationStatics.REGEX_HTTP.
                                    concat(secondData[ApplicationStatics.APP_ZERO]);
                            sitePath = sitePath.strip().split(ApplicationStatics.REGEX_HTML)[ApplicationStatics.APP_ZERO];
                            if (checkSystemUrl(sitePath)) {
                                log.debug("System site: {}", sitePath);
                                getRepository().saveSysSite(new SystemSiteEntity(sitePath));
                            } else if (getRepository().checkSite(sitePath)) {
                                log.debug("Full site: {}", sitePath);
                                getRepository().saveSite(
                                        new SiteEntity(sitePath, siteEntity.getParentUrl(), siteEntity.getName()));
                            }
                        }
                    }
                }
            }
        } else {
            getRepository().replaceEntity(siteEntity);
            getRepository().saveSysSite(new SystemSiteEntity(siteEntity.getUrl()));
        }
    }

    private void buildDataWords(SiteEntity siteEntity, HashSet<String> lemmas, RegisteredSite registeredSite) {
        if (getStop()) {
            for (String incomeString : lemmas) {
                if ((incomeString.length() > ApplicationStatics.MIN_WORD_SIZE)) {
                    WordEntity checked = getRepository().findByWord(incomeString);
                    if (checked.getWord() == null) {
                        checked.setWord(incomeString);
                    }
                    checked.getParentsUrl().add(siteEntity.getParentUrl());
                    checked.getSites().add(registeredSite);
                    getRepository().saveWord(checked);
                }
            }
        }
    }

    private boolean checkSystemFile(String dataIncome, String link, String text) {
        if (!dataIncome.equals("bad")) {
            if (!text.equals("empty")) {
                return !text.isBlank();
            } else {
                    log.debug("System site after: {}", dataIncome);
                    getRepository().saveSysSite(new SystemSiteEntity(link));
                    return false;
            }
        } else {
            log.debug("Bad site: {}", dataIncome);
            getRepository().saveBadSite(new BadSiteEntity(link));
            return false;
        }
    }

    private boolean checkSystemUrl(String url) {
        return (url.contains("{") ||
                url.contains("}")) ||
                url.contains(";") ||
                (url.contains("@") ||
                        url.contains("?")) ||
                url.substring(url.length() - 6).contains(".");
    }
}

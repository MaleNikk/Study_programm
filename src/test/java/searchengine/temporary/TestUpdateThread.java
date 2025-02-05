package searchengine.temporary;

import searchengine.dto.entity.ModelParentSite;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.List;

public final class TestUpdateThread extends Thread{

    private final AppManagementRepositoryImpl repository;

    private final List<ModelParentSite> parentSites;

    private final String command;

    public TestUpdateThread(AppManagementRepositoryImpl repository, List<ModelParentSite> parentSites, String command) {
        this.repository = repository;
        this.parentSites = parentSites;
        this.command = command;
    }

    @Override
    public void run() {
        switch (command) {
            case "save" -> repository.saveParentSites(parentSites);
            case "update" -> {
                for (ModelParentSite parentSite : parentSites) {
                    repository.saveStatistics(parentSite.url(), parentSite.lemmas(), parentSite.pages(), parentSite.status());
                }
            }
            case "delete" -> {
                for (ModelParentSite parentSite : parentSites) {
                    repository.delete(parentSite.url());
                }
            }
            default -> throw new RuntimeException("Not initialise test data!");
        }
    }
}

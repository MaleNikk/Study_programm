package searchengine.configuration;

import searchengine.config.FixedValue;
import searchengine.dto.entity.ModelParentSite;
import searchengine.repository.RepositoryProject;

import java.util.List;

public final class TestUpdateThread extends Thread{

    private final RepositoryProject repository;

    private final List<ModelParentSite> parentSites;

    private final String command;

    public TestUpdateThread(RepositoryProject repository, List<ModelParentSite> parentSites, String command) {
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
                    repository.saveStatistics(parentSite.getUrl(), parentSite.getLemmas(),
                            parentSite.getPages(), FixedValue.IN_PROGRESS);
                }
            }
            case "delete" -> {
                for (ModelParentSite parentSite : parentSites) {
                    repository.delete(parentSite.getUrl());
                }
            }
            default -> throw new RuntimeException("Not initialise test data!");
        }
    }
}

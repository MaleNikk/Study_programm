package searchengine.searching.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.configuration.TestConfiguration;
import searchengine.dto.entity.ModelParentSite;
import searchengine.dto.entity.ModelWord;
import searchengine.configuration.TestCreatorModel;
import searchengine.configuration.TestSaveThread;
import searchengine.configuration.TestUpdateThread;

import java.util.List;
import java.util.concurrent.*;

public final class TestRepository extends TestConfiguration {

    @Test
    @DisplayName("Test multithreading save to Db")
    public void whenSaveEntity_thenReturnListSaved() {
        ConcurrentLinkedQueue<List<ModelWord>> forSaved = TestCreatorModel.LINKED_QUEUE;
        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR.submit(new TestSaveThread(forSaved, repository));
        });
        Assertions.assertNotNull(repository.showIndexedWords());
        System.out.println("\nTest multithreading save complete successful.");
    }

    @Test
    @DisplayName("Test multithreading update to Db")
    public void whenUpdateEntity_thenReturnUpdatedObject() {
        String command = "save";
        List<ModelParentSite> parentSites = TestCreatorModel.PARENT_SITE_LIST_SAVE;

        List<ModelParentSite> parentSitesUpdated = TestCreatorModel.PARENT_SITE_LIST_UPDATE;

        List<ModelParentSite> saved;

        List<ModelParentSite> updated;

        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR
                    .submit(new TestUpdateThread(repository, parentSites, command));
        });
        saved = repository.getParentSites();

        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR
                    .submit(new TestUpdateThread(repository, parentSitesUpdated, "update"));
        });
        updated = repository.getParentSites();

        for (int i = 0; i < updated.size(); i++) {
            ModelParentSite savedSite = saved.get(i);
            ModelParentSite updatedSite = updated.get(i);
            Assertions.assertEquals(savedSite, updatedSite);
            Assertions.assertNotEquals(savedSite.lemmas(), updatedSite.lemmas());
            Assertions.assertNotEquals(savedSite.pages(), updatedSite.pages());
        }
        System.out.println("\nTest multithreading update complete successful.");
    }

    @Test
    @DisplayName("Test multithreading delete from Db")
    public void whenDeleteEntity_thenReturnIsPresent() {

        List<ModelParentSite> parentSites = TestCreatorModel.PARENT_SITE_LIST_SAVE;

        String commandSave = "save";

        String commandDelete = "delete";

        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR.submit(new TestUpdateThread(repository, parentSites, commandSave));
        });

        Assertions.assertNotNull(repository.getParentSites());

        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR.submit(new TestUpdateThread(repository, parentSites, commandDelete));
        });

        Assertions.assertEquals(List.of(),repository.getParentSites());
        System.out.println("\nTest multithreading delete complete successful.");
    }
}

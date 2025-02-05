package searchengine.searching.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import searchengine.dto.entity.ModelParentSite;
import searchengine.dto.entity.ModelWord;
import searchengine.temporary.TestCreatorModel;
import searchengine.temporary.TestSaveThread;
import searchengine.temporary.TestUpdateThread;

import java.util.List;
import java.util.concurrent.*;

public final class TestRepository {

    private final AppManagementRepositoryImpl repository = Mockito.mock(AppManagementRepositoryImpl.class);

    @Test
    @DisplayName("Test multithreading save to Db")
    public void whenSaveEntity_thenReturnListSaved() {
        ConcurrentLinkedQueue<List<ModelWord>> forSaved = TestCreatorModel.LINKED_QUEUE;
        Assertions.assertDoesNotThrow(() ->
        {
            TestCreatorModel.TEST_EXECUTOR.submit(new TestSaveThread(forSaved, repository));
        });
        Assertions.assertNotNull(repository.showIndexedWords());
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
    }
}

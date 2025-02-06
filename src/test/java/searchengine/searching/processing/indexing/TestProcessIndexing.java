package searchengine.searching.processing.indexing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.configuration.TestConfiguration;

public final class TestProcessIndexing extends TestConfiguration {

    @Test
    @DisplayName("Test initialise indexing manager")
    public void whenStartApplication_thenReturnNonNullObject(){
        Assertions.assertNotNull(indexingManager);
        System.out.println("\nTest init manager indexing complete successful.");
    }
}

package searchengine.searching.processing.searching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.configuration.TestConfiguration;

public final class TestProcessSearching extends TestConfiguration {

    @Test
    @DisplayName("Test initialise searching object")
    public void whenStartApplication_thenReturnNonNullObject(){
        Assertions.assertNotNull(searchingManager);
        System.out.println("\nTest init searching manager complete successfully!");
    }
}

package searchengine.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.configuration.TestConfiguration;

public final class TestProcessSearching extends TestConfiguration {

    @Test
    @DisplayName("Test initialise searching object")
    public void whenStartApplication_thenReturnNonNullObject(){
        Assertions.assertNotNull(serviceFindingData);
        System.out.println("\nTest init searching manager complete successfully!");
    }
}

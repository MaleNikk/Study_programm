package searchengine.searching.processing.morphology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.searching.processing.morhpology.ProjectMorphology;

public final class TestMorhpology {

    private final ProjectMorphology projectMorphology = new ProjectMorphology();

    @Test
    @DisplayName("Test morphology with normal words")
    public void whenPushWord_thenReturnBasicWord(){

        if (projectMorphology.isWithMorphology()) {

            String testRuWord = "камни";
            String testEnWord = "pancakes";

            String expectedRuWord = "камень";
            String expectedEnWord = "pancake";

            Assertions.assertEquals(expectedRuWord, projectMorphology.getForm(testRuWord));

            Assertions.assertEquals(expectedEnWord, projectMorphology.getForm(testEnWord));
        }

        System.out.println("\nTest morphology with normal words complete successful.");
    }

    @Test
    @DisplayName("Test morphology with part of speech")
    public void whenPushPart_thenReturnEmptyWord(){

        if (projectMorphology.isWithMorphology()) {

            String testWord_1 = "либо";
            String testWord_2 = "благодаря";
            String testWord_3 = "что";

            String expectedWord = " ";

            Assertions.assertEquals(expectedWord, projectMorphology.getForm(testWord_1));

            Assertions.assertEquals(expectedWord, projectMorphology.getForm(testWord_2));

            Assertions.assertEquals(expectedWord, projectMorphology.getForm(testWord_3));
        }

        System.out.println("\nTest morphology with parts of speech complete successful.");
    }
}

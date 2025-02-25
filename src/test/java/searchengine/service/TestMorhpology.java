package searchengine.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.config.AppProperties;
import searchengine.logic.LemmaCreator;

public final class TestMorhpology {

    private final LemmaCreator lemmaCreator;

    public TestMorhpology() {
        lemmaCreator = new LemmaCreator(new AppProperties(true, 5));
    }

    @Test
    @DisplayName("Test morphology with normal words")
    public void whenPushWord_thenReturnBasicWord(){
            String testRuWord = "камни";
            String testEnWord = "pancakes";

            String expectedRuWord = "камень";
            String expectedEnWord = "pancake";

            Assertions.assertEquals(expectedRuWord, lemmaCreator.getForm(testRuWord));

            Assertions.assertEquals(expectedEnWord, lemmaCreator.getForm(testEnWord));

        System.out.println("\nTest morphology with normal words complete successful.");
    }

    @Test
    @DisplayName("Test morphology with part of speech")
    public void whenPushPart_thenReturnEmptyWord(){

            String testWord_1 = "либо";
            String testWord_2 = "благодаря";
            String testWord_3 = "что";

            String expectedWord = "";

            Assertions.assertEquals(expectedWord, lemmaCreator.getForm(testWord_1));

            Assertions.assertEquals(expectedWord, lemmaCreator.getForm(testWord_2));

            Assertions.assertEquals(expectedWord, lemmaCreator.getForm(testWord_3));

    }
}

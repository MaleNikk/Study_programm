package searchengine.searching.processing.searching;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class CalculateRelevance {

    private double relevance = 0.0;

    public double foundRelevance(String searchWord, String savedWord, int countWords) {

        char[] charsSearch = searchWord.toCharArray();

        char[] charsSaved = savedWord.toCharArray();

        int minLength = Math.min(charsSearch.length, charsSaved.length);

        int maxLength = Math.max(charsSearch.length, charsSaved.length);

        double relevance = maxLength - minLength > 1 ? 3.0 : 1.5;

        for (int i = 0; i < minLength; i++) {

            if (Objects.equals(charsSearch[i], charsSaved[i])) {

                relevance -= 0.1;
            }
        }
        return (relevance - (double)countWords/100) - getRelevance();
    }
}

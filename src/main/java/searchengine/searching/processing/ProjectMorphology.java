package searchengine.searching.processing;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public final class ProjectMorphology {

    public String getForm(String word) {
        List<String> words = List.of();
        try {
            LuceneMorphology morphologyRu = new RussianLuceneMorphology();

            LuceneMorphology morphologyUs = new EnglishLuceneMorphology();

            if (Pattern.compile("[a-zA-Z]+").matcher(word).find()){
                if (morphologyUs.checkString(word)) {
                        words = morphologyUs.getNormalForms(word);
                }
            } else {
                if (morphologyRu.checkString(word)) {
                    String info = morphologyRu.getMorphInfo(word).get(FixedValue.ZERO);
                    if (!(info.contains("СОЮЗ") || info.contains("ПРЕДЛ") || info.contains("ЧАСТ"))) {
                        words = morphologyRu.getNormalForms(word);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return words.isEmpty() ? " " : words.get(FixedValue.ZERO);
    }
}

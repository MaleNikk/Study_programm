package searchengine.searching.processing.morhpology;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import searchengine.searching.processing.constant.FixedValue;

import java.io.IOException;
import java.util.List;

public final class ProjectMorphology {

    public String getForm(String word) {
        List<String> words = List.of();

        try {
            LuceneMorphology morphologyRu = new RussianLuceneMorphology();

            LuceneMorphology morphologyUs = new EnglishLuceneMorphology();

            if (checkEn(word)){
                if (morphologyUs.checkString(word)) {
                        words = morphologyUs.getNormalForms(word);
                }
            } if (checkRu(word)) {
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

    private boolean checkRu(String word){
        int[] charsRu = {1040,1104,1105,1025};
        char[] wordChars = word.toCharArray();
        int firstChar = wordChars[FixedValue.ZERO];
        return (firstChar <= charsRu[1] && firstChar >= charsRu[0])||firstChar == charsRu[2]||firstChar == charsRu[3];
    }

    private boolean checkEn(String word){
        int[] charsEn = {65,90,97,122};
        char[] wordChars = word.toCharArray();
        int firstChar = wordChars[FixedValue.ZERO];
        return (firstChar >= charsEn[0] && firstChar <= charsEn[1])||(firstChar >= charsEn[2]&&firstChar <= charsEn[3]);
    }
}

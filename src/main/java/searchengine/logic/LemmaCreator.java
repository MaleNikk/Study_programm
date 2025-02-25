package searchengine.logic;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.config.FixedValue;
import searchengine.config.AppProperties;

import java.io.IOException;

@Getter
@Setter
@Log4j2
@Component
public final class LemmaCreator {

    private AppProperties status;

    public LemmaCreator(@Autowired AppProperties status) {
        this.status = status;
    }

    public String getForm(String word) {
        String clearWord = word.replaceAll(FixedValue.REGEX_NO_ABC, "").toLowerCase();
        if (clearWord.length() < 3 || clearWord.length() > 25){
            return "";
        }
        if (status.isStatus()) {
            try {
                if (checkEn(clearWord)) {
                    return getEnLemma(clearWord);
                }
                if (checkRu(clearWord)) {
                    return getRuLemma(clearWord);
                }
            } catch (IOException e) {
                log.debug("Invalid word: {}", clearWord);
                return "";
            }
        }
        return clearWord.length() < 6 ? clearWord.substring(0,  clearWord.length() - 1) :
                clearWord.substring(0, clearWord.length() - 2);
    }

    private String getRuLemma(String word) throws IOException {
        LuceneMorphology morphologyRu = new RussianLuceneMorphology();
        String info = morphologyRu.getMorphInfo(word).get(FixedValue.ZERO);
        if (morphologyRu.checkString(word) && !(info.contains("СОЮЗ")||info.contains("ПРЕДЛ")||info.contains("ЧАСТ"))){
            return morphologyRu.getNormalForms(word).get(FixedValue.ZERO);
        }
        return "";
    }

    private String getEnLemma(String word) throws IOException {
        LuceneMorphology morphologyUs = new EnglishLuceneMorphology();
        if (!morphologyUs.checkString(word)) {
            return "";
        }
        return morphologyUs.getNormalForms(word).get(FixedValue.ZERO);
    }


    private boolean checkRu(String word) {
        int[] charsRu = {1040, 1104, 1105, 1025};
        int first = word.charAt(0);
        return (first <= charsRu[1] && first >= charsRu[0]) || first == charsRu[2] || first == charsRu[3];
    }

    private boolean checkEn(String word) {
        int[] charsEn = {65, 90, 97, 122};
        int first = word.charAt(0);
        return (first >= charsEn[0] && first <= charsEn[1])||(first >= charsEn[2] && first <= charsEn[3]);
    }
}

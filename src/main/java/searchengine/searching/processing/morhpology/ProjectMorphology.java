package searchengine.searching.processing.morhpology;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import searchengine.searching.processing.constant.FixedValue;

import java.io.IOException;
import java.util.List;

@Getter
@Log4j2
public final class ProjectMorphology {

    private final boolean withMorphology = false;

    public String getForm(String word) {

        String clearWord = word.replaceAll(FixedValue.REGEX_ABC, "").toLowerCase();

        if (clearWord.length() > 2 && clearWord.length() < 17 && clearWord.length() >= word.length() - 2) {

            if (withMorphology) {

                List<String> words = List.of();

                try {
                    LuceneMorphology morphologyRu = new RussianLuceneMorphology();

                    LuceneMorphology morphologyUs = new EnglishLuceneMorphology();

                    if (checkEn(clearWord)) {

                        if (morphologyUs.checkString(clearWord)) {

                            words = morphologyUs.getNormalForms(clearWord);
                        }
                    }
                    if (checkRu(clearWord)) {

                        if (morphologyRu.checkString(clearWord)) {

                            String info = morphologyRu.getMorphInfo(clearWord).get(FixedValue.ZERO);

                            if (!(info.contains("СОЮЗ") || info.contains("ПРЕДЛ") || info.contains("ЧАСТ"))) {

                                words = morphologyRu.getNormalForms(clearWord);
                            }
                        }
                    }

                } catch (IOException e) {

                    log.debug("Word is invalid or morphology don't find word '{}' in library", word);

                    return " ";
                }
                return words.isEmpty() ? " " : words.get(FixedValue.ZERO);
            }
            return clearWord.length() <= 5 ? clearWord.substring(0, 3) : clearWord.substring(0, clearWord.length()-2);
        }
        return " ";
    }


    private boolean checkRu(String word) {

        int[] charsRu = {1040, 1104, 1105, 1025};

        char[] wordChars = word.toCharArray();

        int firstChar = wordChars[FixedValue.ZERO];

        return (firstChar <= charsRu[1] && firstChar >= charsRu[0]) || firstChar == charsRu[2] || firstChar == charsRu[3];
    }

    private boolean checkEn(String word) {

        int[] charsEn = {65, 90, 97, 122};

        char[] wordChars = word.toCharArray();

        int firstChar = wordChars[FixedValue.ZERO];

        return (firstChar >= charsEn[0] && firstChar <= charsEn[1]) || (firstChar >= charsEn[2] && firstChar <= charsEn[3]);
    }
}

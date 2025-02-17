package searchengine.searching.processing.searching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class SnippetBuilder {

    private final AtomicInteger countWords;

    private final HashSet<String> findLemmas;

    private final CalculateRelevance relevance;

    public SnippetBuilder(AtomicInteger countWords, HashSet<String> findLemmas, CalculateRelevance relevance) {
        this.countWords = countWords;
        this.findLemmas = findLemmas;
        this.relevance = relevance;
    }

    public String foundSnippets(String text, String lemma) {

        HashSet<String> snippets = new HashSet<>();

        ArrayList<String> words = new ArrayList<>();

        Scanner scanner = new Scanner(text).useDelimiter("\\s+");

        StringBuilder result = new StringBuilder();

        while (scanner.hasNext()){

            String find = scanner.next();

            if (!result.isEmpty()) {

                words.add(find);

                if (words.size() == 7 || !scanner.hasNext()) {

                    for (String word : words){
                        for (String anyLemma : findLemmas) {
                            if (!word.toLowerCase().startsWith(lemma) && word.toLowerCase().startsWith(anyLemma)){
                                word = " <b>".concat(word.toUpperCase()).concat("</b>");
                                relevance.setRelevance(relevance.getRelevance() + 0.3);
                            }
                        }
                        result.append(" ").append(word);
                    }

                    result.append(".....<br>");

                    snippets.add(result.toString());

                    words.clear();

                    result = new StringBuilder();
                }
            } else if (find.toLowerCase().startsWith(lemma)){

                int beginIndex = words.size() >= 7 ? words.size()-7 : 0;

                List<String> textPart = words.subList(beginIndex, words.size());

                result.append("<br>.....");

                for (String word : textPart){
                    for (String anyLemma : findLemmas) {
                        if (!word.toLowerCase().startsWith(lemma) && word.toLowerCase().startsWith(anyLemma)){
                            word = " <b>".concat(word.toUpperCase()).concat("</b>");
                            relevance.setRelevance(relevance.getRelevance() + 0.3);
                        }
                    }
                    result.append(" ").append(word);
                }

                result.append(" <b>").append(find.toUpperCase()).append("</b>");

                words.clear();
            } else  {
                words.add(find);
            }
        }
        countWords.set(snippets.size());

        return snippets.toString();
    }
}

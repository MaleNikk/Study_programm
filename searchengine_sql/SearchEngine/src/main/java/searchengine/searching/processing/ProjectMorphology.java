package searchengine.searching.processing;

public class ProjectMorphology {

    public static String getForm(String word) {
        return word.length() > 7 ? word.toLowerCase().substring(FixedValue.ZERO, 6):
                word.length() < 4  ? word.toLowerCase():
                        word.toLowerCase().substring(FixedValue.ZERO, 3);
    }
}

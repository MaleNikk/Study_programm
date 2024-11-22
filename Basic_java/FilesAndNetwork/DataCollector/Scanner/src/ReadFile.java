import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
    private List<String> stringsFromDataString = new ArrayList<>();
    private String stringData = "";

    public void readHTML(String path) throws IOException {
        clearCollection();
        readFile(path);
    }
    public void readCsv(String path) throws IOException {
        clearCollection();
        readFile(path);
    }
    public void readJson(String path) throws IOException {
        clearCollection();
        readFile(path);
    }
    public void readFile(String path) throws IOException {
        List<String> listFromScanner = new ArrayList<>();
        FileReader text = new FileReader(path);
        Scanner scr = new Scanner(text);
        scr.useDelimiter("%n");
        scr.forEachRemaining(listFromScanner::add);
        text.close();
        treatment(listFromScanner);
    }
    public void print() {
        System.out.println(getStringData());
    }
    private void treatment(List<String> data) {
        for (String file : data) {
            if (file != null) {
                stringData = stringData.concat(file);
            }
        }
        stringsFromDataString = List.of(stringData.split("\n"));
    }
    public List<String> getStringsFromDataString() {
        return stringsFromDataString;
    }
    public String getStringData() {
        return stringData;
    }
    public void clearCollection() {
        stringsFromDataString = new ArrayList<>();
        stringData = "";
    }
}

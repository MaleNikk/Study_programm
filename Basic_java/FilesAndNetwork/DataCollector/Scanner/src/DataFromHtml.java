import java.util.*;
import java.util.Map;
import java.util.TreeMap;

public class DataFromHtml {
    private final Map<String, String> nameLinesMetro = new TreeMap<>();
    private final Map<String, String> stationsAndLines = new TreeMap<>();
    private final Map<String, List<String>> linesWithListStations = new TreeMap<>();
    private final List<String> stations = new ArrayList<>();
    private final List<String> stationsWithTransition = new ArrayList<>();
    private final List<String> allStations = new ArrayList<>();

    public void collectLines(List<String> data) {
        String[] strings;
        String lines = "";
        for (String string : data) {
            if ((string.contains("line") || string.contains("линия") || string.contains("Линия")) &&
                    (!(string.replaceAll("[^а-яА-Я]", "").isEmpty()))) {
                lines = lines.concat(string.trim().replaceAll("</div>", "")
                        .replaceAll("data-line=", "").concat(" "));
            }
        }
        strings = lines.split("</span>");
        for (String string : strings) {
            if (!string.trim().isEmpty()) {
                String[] keyAndValue = string.split(">");
                nameLinesMetro.put(keyAndValue[0].replaceAll("\"", "").trim(),
                        keyAndValue[1].trim());
            }
        }
    }

    public void treatment(String dataEntry) {
        String[] strings = dataEntry.split("metro-line");
        for (String string : strings) {
            String[] arrayString = string.split("metro-stations", 2);
            if (arrayString[1].contains("data-line")) {
                stations.add(arrayString[1]);
            }
        }
        clearHtml();
        addMap(getStations());
    }

    private void clearHtml() {
        List<String> dataList = new ArrayList<>();
        for (String treatmentString : getStations()) {
            String line = "";
            String check = "";
            char[] chars = treatmentString.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                check = check.concat(String.valueOf(chars[i]));
                if (check.contains("t-metrostation-list-table\" data-line=\"")) {
                    for (int b = (i + 1); ; b++) {
                        if (chars[b] == '\"') {
                            dataList.add("line: ".concat(line.trim()).concat("="));
                            line = "";
                            check = "";
                            i = b;
                            break;
                        }
                        line = line.concat(String.valueOf(chars[b]));
                    }
                }
                if (check.contains("class=\"name\">")) {
                    for (int b = (i + 1); ; b++) {
                        if (chars[b] == '<') {
                            dataList.add(".".concat(line).trim().concat(" : "));
                            line = "";
                            check = "";
                            i = b;
                            break;
                        }
                        line = line.concat(String.valueOf(chars[b]));
                    }
                }
                if (check.contains("title=")) {
                    for (int b = (i + 1); ; b++) {
                        if (chars[b] == '>') {
                            dataList.add(line.trim().concat(", "));
                            line = "";
                            check = "";
                            i = b;
                            break;
                        }
                        line = line.concat(String.valueOf(chars[b]));
                    }
                }
            }
        }
        stations.clear();
        dataList.forEach(text -> {
            String[] strings = text.split("\\s+");
            String add = "";
            for (String string : strings) {
                add = add.concat(string.trim()).concat(" ");
            }
            stations.add(add);
        });
    }

    private void addMap(List<String> stringList) {
        String allStrings = "";
        for (String string : stringList) {
            allStrings = allStrings.concat(" ").concat(string);
        }
        String[] dataStrings = allStrings.split("line: ");
        for (String keysAndValues : dataStrings) {
            if (keysAndValues.contains("=")) {
                String[] addMap = keysAndValues.split("=", 2);
                List<String> listStations = List.of(addMap[1].split("\\."));
                linesWithListStations.put(addMap[0].trim(), listStations);
                for (String string : listStations) {
                    if (string.contains("переход")) {
                        stationsWithTransition.add(string);
                    }
                    if (!string.trim().isEmpty()) {
                        allStations.add(string.concat(" = ").concat(getNameLinesMetro().get(addMap[0].trim())));
                        stationsAndLines.put(string, getNameLinesMetro().get(addMap[0].trim()));
                    }
                }
            }
        }

    }

    public boolean hasConnect(String station) { return getStationsWithTransition().contains(station); }

    public void print() {
        System.out.println("\n".concat(" Lines metro with number: ").concat("\n"));
        for (Map.Entry<String, String> map : getNameLinesMetro().entrySet()) {
            System.out.println(map.getKey().concat(" - ").concat(map.getValue()));
        }
        System.out.println("\n".concat(" Stations metro with number lines: ").concat("\n"));
        for (Map.Entry<String, String> entry : getStationsAndLines().entrySet()) {
            System.out.println(entry.getKey().concat(" - ").concat(entry.getValue()));
        }
        System.out.println("\n".concat(" Stations metro with transition: ").concat("\n"));
        getStationsWithTransition().forEach(System.out::println);
    }

    public List<String> getStations() {
        return stations;
    }

    public Map<String, String> getNameLinesMetro() {
        return nameLinesMetro;
    }

    public Map<String, String> getStationsAndLines() {
        return stationsAndLines;
    }

    public List<String> getStationsWithTransition() {
        return stationsWithTransition;
    }

    public List<String> getAllStations() {
        return allStations;
    }

    public Map<String, List<String>> getLinesWithListStations() {
        return linesWithListStations;
    }
}

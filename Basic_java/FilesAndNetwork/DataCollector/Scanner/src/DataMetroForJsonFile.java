import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataMetroForJsonFile {
    private String stationsInLines = "";
    private String stationTransition = "";
    private String nameLines = "";

    public void mapStationsInLines(Map<String, List<String>> mapMetro) {
        List<String> files = new ArrayList<>();
        String string = "";
        int sizeMap = mapMetro.size();
        for (Map.Entry<String, List<String>> entry : mapMetro.entrySet()) {
            sizeMap--;
            string = string.concat("\t\t\"").concat(entry.getKey()).concat("\": ");
            for (String file : entry.getValue()) {
                if (!file.trim().isEmpty()) {
                    String[] strings = file.split(":", 2);
                    String add = "\"".concat(strings[0]).concat("\"");
                    files.add("\n\t\t\t".concat(add));
                }
            }
            if (sizeMap > 0) {
                stationsInLines = stationsInLines.concat(string).concat(String.valueOf(files).concat(",").concat("\n"));
            } else {
                stationsInLines = stationsInLines.concat(string).concat(String.valueOf(files).concat("\n"));
            }
            files.clear();
            string = "";
        }
    }

    public void mapStationsWithTransition(List<String> stationsWithTransition) {
        List<String> connections = new ArrayList<>();
        for (String connect : stationsWithTransition) {
            if (connect.contains("переход")) {
                connections.add(connect.replaceAll("[^а-яА-Я,-:=]", " ").trim());
            }
        }
        int countStations = connections.size();
        for (String station : connections) {
            countStations--;
            String nameStation = "";
            String nameLine = "";
            String transition = "\n\t\t\t\"connect\": \"";
            String[] nameData = station.split(":", 2);
            String[] lineData = nameData[1].split("=", 2);
            String[] transitionData = lineData[0].split(",");
            List<String> listConnections = new ArrayList<>();
            for (String connect : transitionData) { if (!connect.trim().isEmpty()) { listConnections.add(connect); } }
            int countConnect = listConnections.size();
            for (String connect : listConnections) {
                countConnect--;
                if (countConnect>0) {
                    transition = transition.concat(connect.trim()).concat(", ");
                } else { transition = transition.concat(connect.trim()).concat("\""); }
            }
            nameStation = nameStation.concat(nameData[0].trim());
            nameLine = nameLine.concat(lineData[1].trim());
            if (countStations > 0) {
                stationTransition = stationTransition.
                        concat("\n\t\t\t{\n\t\t\t\"line\": \"").concat(nameLine).concat("\",\n").
                        concat("\t\t\t\"station\": \"").concat(nameStation).concat("\",").
                        concat(transition).concat("\n\t\t\t},");
            }
            if (countStations == 0) {
                stationTransition = stationTransition.
                        concat("\n\t\t\t{\n\t\t\t\"line\": \"").concat(nameLine).concat("\",\n").
                        concat("\t\t\t\"station\": \"").concat(nameStation).concat("\",").
                        concat(transition).concat("\n\t\t\t}");
            }
        }
    }

    public void mapNameLinesMetro(Map<String, String> nameLinesMetro) {
        int mapSize = nameLinesMetro.size();
        for (Map.Entry<String, String> entry : nameLinesMetro.entrySet()) {
            mapSize--;
            if (mapSize == 0) {
                nameLines = nameLines.concat("\t\t\t{\n\t\t\t\"Код линии\" : \"").concat(entry.getKey()).
                        concat("\",\n\t\t\t\"Название линии\" : \"").concat(entry.getValue().
                                concat("\"\n\t\t\t}\n"));
            } else {
                nameLines = nameLines.concat("\t\t\t{\n\t\t\t\"Код линии\" : \"").concat(entry.getKey()).
                        concat("\",\n\t\t\t\"Название линии\" : \"").concat(entry.getValue().
                                concat("\"\n\t\t\t},\n"));
            }
        }
    }

    public String getStationsInLines() {
        return stationsInLines;
    }

    public String getStationTransition() {
        return stationTransition;
    }

    public String getNameLines() {
        return nameLines;
    }
}

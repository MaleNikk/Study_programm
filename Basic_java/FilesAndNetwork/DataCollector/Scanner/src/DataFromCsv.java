import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataFromCsv {
    private final Map<String, String> stations = new TreeMap<>();
    public void createDataFromFile (List<String> data){
        for (String file: data) {
            String[] dataForMap = file.split(",");
            stations.put(dataForMap[0].trim(),dataForMap[1].trim());
        }
    }
    public Map<String, String> getStations() { return stations; }
    public void print () {
        for (Map.Entry<String,String> stations: getStations().entrySet()) {
            System.out.println(stations.getKey().concat(" - ").concat(stations.getValue()).concat("\n"));
        }
    }
}

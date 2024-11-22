import java.util.*;
import java.util.Map;
import java.util.TreeMap;
public class DataFromJson {
    private final Map<String, String> dataFromJsonFile = new TreeMap<>();
    public void collection(String dataEntry, char divider) {
        List<String> dataList = getStrings(dataEntry, divider);
        String key = "";
        String value = "";
        int indexKey = 0;
        for (int i = 0; i < dataList.size(); i++) {
            indexKey++;
            if (dataList.get(i).equals("station_name")) {
                key = dataList.get(i + 1);
            }
            if (dataList.get(i).equals("depth")) {
                value = dataList.get(i + 1);
            }
            if (indexKey == 2) {
                dataFromJsonFile.put(key.trim(),value.trim());
                indexKey = 0;
            }
        }
    }
    private List<String> getStrings(String dataEntry, char divider) {
        char[] chars = dataEntry.toCharArray();
        List<String> dataList = new ArrayList<>();
        StringBuilder stringBuilder;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == divider) {
                stringBuilder = new StringBuilder();
                for (int b = (i + 1); ; b++) {
                    if (chars[b] == divider) {
                        i = b;
                        break;
                    }
                    stringBuilder.append(chars[b]);
                }
                dataList.add(String.valueOf(stringBuilder));
            }
        }
        return dataList;
    }
    public void print() {
        for (Map.Entry<String, String> map : getDataFromJsonFile().entrySet()) {
            System.out.println(map.getKey().concat(" - ").concat(map.getValue()));
        }
    }
    public Map<String, String> getDataFromJsonFile() {
        return dataFromJsonFile;
    }

}

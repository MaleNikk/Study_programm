package org.homework;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class TreatmentDataFromDB {
    private List<String> endWorkWithData;

    public void workWithData(List<String> listQuery, Map<String, List<String>> dataMap) {
        endWorkWithData = new ArrayList<>();
        String queryKey = listQuery.get(listQuery.size() - 1);
        List<String> dataStrings = dataMap.get(queryKey);
        dataStrings.add(" ");
        int countData = 0;
        int month = 1;
        double indexPurchase;
        for (int i = 0; i < dataStrings.size() - 1; i++) {
            String data = dataStrings.get(i);
            String check = stringFromString(dataStrings.get(i+1));
            if (data.contains("2018")) {
                countData++;
                if (month < digitalMonth(data)) {
                    month = digitalMonth(data);
                }
                if (!(data.contains(check))) {
                indexPurchase = (double) countData / month;
                String index = String.format("%.3f", indexPurchase);
                endWorkWithData.add(stringFromString(data).concat(" - Индекс количества покупок: ").concat(index));
                countData = 0;
                month = 1;
                }
            }
        }
    }

    public void print() {
        getEndWorkWithData().forEach(System.out::println);
    }
    private String stringFromString(String string) {
        String[] array = string.split(">");
        return array[0];
    }
    private int digitalMonth(String string) {
        String[] data = string.split("2018");
        String[] dataDate = data[1].split("-");
        return Integer.parseInt(dataDate[1]);
    }
}

package org.homework;

import lombok.Getter;

import java.util.*;

public class StorageQuery {
    @Getter
    private final Calendar calendar = Calendar.getInstance();
    private int countQuery;
    @Getter
    private final Map<String, List<String>> storage = new TreeMap<>();
    @Getter
    private final List<String> queryList = new ArrayList<>();
    public void addStorage(String textQuery, List<String> listQuery) {
        countQuery++;
        String key = "[ Number query: ".
                concat(String.valueOf(countQuery)).
                concat(" ,\n ").
                concat(String.valueOf(calendar.getTime())).
                concat(" ]:\n\t\"").
                concat(textQuery).
                concat("\" :\n\t{");
        storage.put(key, listQuery);
        queryList.add(key);
    }
    public void lookStorageQuery() {
        System.out.println("Список запросов из базы данных: ");
        for (Map.Entry<String,List<String>> stringObjectEntry:getStorage().entrySet()) {
            System.out.println(stringObjectEntry.getKey());
            for (String string:stringObjectEntry.getValue()) {
                System.out.println("\t\t".concat(string));
            }
            System.out.println("\t}");
        }
    }
    public void LookQueryList() {
        getQueryList().forEach(System.out::println);
    }
}

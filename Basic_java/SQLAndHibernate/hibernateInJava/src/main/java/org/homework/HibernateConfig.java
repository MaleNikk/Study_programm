package org.homework;

import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
public class HibernateConfig {
    public HibernateConfig() {}
    public HibernateConfig(String url, String name, String password, String nameDatabase) {
        this.url = url;
        this.name = name;
        this.password = password;
        this.nameDatabase = nameDatabase;
    }

    private String
            url = "url",
            name = "name",
            password = "password",
            nameDatabase = "nameDatabase",
            propertyFile,
            userInfo;
    private final String
            examplePropertyFile = "src/main/example/hibernate.cfg.xml",
            property = "src/main/resources/hibernate.cfg.xml",
            info = "src/main/resources/userInfo.txt";
    private List<String> incomeFiles;
    private void readPropertyFile() throws IOException {
        setIncomeFiles(new ArrayList<>());
        FileReader text = new FileReader(getExamplePropertyFile());
        Scanner scr = new Scanner(text);
        scr.useDelimiter("%n");
        scr.forEachRemaining(getIncomeFiles()::add);
        text.close();
        scr.close();
    }
    private void treatmentPropertyFile(String indexInfo) throws IOException {
        setUserInfo(getUrl().concat("\n").
                concat(getName()).concat("\n").
                concat(getPassword()).concat("\n").
                concat(getNameDatabase()).concat("\n").
                concat(indexInfo));
        if (!getUrl().contains("url")) {
            String[] partsUrl = getUrl().split("/");
            setUrl("");
            for (int i = 0; i <= partsUrl.length; i++) {
                if (i < (partsUrl.length - 1)) {
                    setUrl(getUrl().concat(partsUrl[i]).concat("/"));
                }
                if (i == partsUrl.length) {
                    setUrl(getUrl().concat(getNameDatabase()));
                    break;
                }
            }
        }
        readPropertyFile();
        setPropertyFile(buildPropertyFile());
        System.out.println("\n\nФормирование файла конфигурации Hibernate:\n\n".
                concat(buildPropertyFile()).concat("\n\nФайл данных пользователя:\n\n").concat(getUserInfo()));
    }
    private String buildPropertyFile() {
        String propertyFile;
        String[] part1 = getIncomeFiles().get(0).split("url\">",2);
        String[] part2 = part1[1].split("<", 2);
        propertyFile = part1[0].concat("url\">").concat(getUrl()).
                concat("?allowPublicKeyRetrieval=true<").concat(part2[1]);
        String[] part3 = propertyFile.split("username\">",2);
        String[] part4 = part3[1].split("<",2);
        propertyFile = part3[0].concat("username\">").concat(getName()).concat("<").concat(part4[1]);
        String[] part5 = propertyFile.split("password\">",2);
        String[] part6 = part5[1].split("<",2);
        propertyFile = part5[0].concat("password\">").concat(getPassword()).concat("<").concat(part6[1]);
        return propertyFile;
    }
    public void createPropertyFile(String indexInfo) throws IOException {
        treatmentPropertyFile(indexInfo);
        writePropertyFile(new FileWriter(getProperty()), getPropertyFile());
        writePropertyFile(new FileWriter(getInfo()), getUserInfo());
    }
    private void writePropertyFile(FileWriter fileWriter, String file) throws IOException {
        fileWriter.write(file);
        fileWriter.close();
    }
}

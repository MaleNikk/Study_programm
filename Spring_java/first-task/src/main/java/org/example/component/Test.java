package org.example.component;

import java.io.File;
public class Test {
    public Test(){
        System.out.println("Test initial.");
    }
    public boolean testString(String data){
        return data.toLowerCase().
                replaceAll("[a-z]","").
                replaceAll("[а-я]","").isEmpty();
    }
    public boolean testPhone (String data){
        return data.replaceAll("[0-9+-]","").isEmpty();
    }
    public boolean testEmail(String data){
        return data.toLowerCase().
                replaceAll("[a-z]","").
                replaceAll("[а-я]","").
                replace("@","").
                replace(".","").
                replaceAll("[0-9]","").isEmpty();

    }
    public boolean testFile(String pathFile){
        File file = new File(pathFile);
        return file.exists();
    }
}

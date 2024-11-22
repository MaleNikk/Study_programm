package com.example.skillboxsecondtask.testing;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class Logger {
    private final String pathError = "src/main/resources/logFiles/error.txt";
    private final String pathDebug = "src/main/resources/logFiles/debug.txt";
    private final String pathInfo = "src/main/resources/logFiles/info.txt";

    public void error(String info) {
        String message = "\nError info: ".concat("Time: ").concat(String.valueOf(Instant.now()).concat("\n\t").
                concat(info).concat("\n"));
        if (checkFile(pathError)) {
            writeLog(file(pathError).concat(message), pathError);
        } else {
            writeLog("\nFile for save error in code!\n", pathError);
        }
    }

    public void info(String info) {
        String message = "\nWork info: ".concat("Time: ").concat(String.valueOf(Instant.now()).concat("\n\t").
                concat(info).concat("\n"));
        if (checkFile(pathInfo)) {
            writeLog(file(pathInfo).concat(message), pathInfo);
        } else {
            writeLog("\nFile for save information about working code!\n", pathInfo);
        }
    }

    public void debug(String info) {
        String message = "\nDebug info: ".concat("Time: ").concat(String.valueOf(Instant.now()).concat("\n\t").
                concat(info).concat("\n"));
        if (checkFile(pathDebug)) {
            writeLog(file(pathDebug).concat(message), pathDebug);
        } else {
            writeLog("\nFile for save debug point in code!\n", pathDebug);
        }
    }

    public void writeLog(String message, String path) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String file(String pathFile) {
        String data = "";
        try {
            List<String> files = Files.readAllLines(Paths.get(pathFile));
            for (String s : files) {
                if (!s.strip().trim().isEmpty()) {
                    data = data.concat("\n".concat(s));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private boolean checkFile(String path) {
        return new File(path).exists();
    }
}

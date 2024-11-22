import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteJsonFile {
    private String source;
    private FileWriter fileWriter;

    public WriteJsonFile() throws IOException {
    }

    public void writeFileStations(String keyName, List<String> data, String fileName) throws IOException {
        setSource("{\n\t\"".concat(keyName).concat("\" :").
                concat("\t\t".concat(String.valueOf(data))).concat("\n}"));
        writeFile(getSource(), fileName);
    }

    public void writeFileMap(String dataStations,
                             String dataConnections,String dataLines, String fileName) throws IOException {
        setSource("{\n\t\"stations\": {\n".
                concat(dataStations).concat("\t},\n").
                concat("\t\"connections\": [").
                concat(dataConnections).concat("\n\t\t],").
                concat("\n\t\t\"lines\": [\n").
                concat(dataLines).concat("\t]").
                concat("\n}"));
        writeFile(getSource(), fileName);
    }


    private void writeFile(String source, String fileName) throws IOException {
        setFileWriter(new FileWriter(fileName));
        fileWriter.write(source);
        fileWriter.close();
    }

    public String getSource() {
        return source;
    }

    private void setSource(String source) {
        this.source = source;
    }

    private void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}

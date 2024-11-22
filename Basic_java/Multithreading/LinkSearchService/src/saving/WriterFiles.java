package saving;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WriterFiles {
    private final String source;
    private final String path;
    public WriterFiles(String source,String path){
        this.source = source;
        this.path = path;
    }

    private void writeFile(String source, String path) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        if (file.exists()) {
            List<String> stringList = Files.readAllLines(Paths.get(path));
            String dataIncome = "";
            for (String string : stringList) {
                dataIncome = dataIncome.concat("\n").concat(string);
            }
            dataIncome = dataIncome.concat(source);
            writer.write(dataIncome);
            writer.close();
            System.out.println("File written.");
        } else {
            writer.write(source);
            writer.close();
            System.out.println("File written.");
        }
    }

    public void writeDataFile() {
        try {
            writeFile(getSource(), getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private String getSource(){ return this.source; }
    private String getPath(){ return this.path; }
}

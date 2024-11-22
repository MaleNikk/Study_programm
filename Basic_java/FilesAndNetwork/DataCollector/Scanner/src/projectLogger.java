import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class projectLogger {
    private String message = "";
    private final Calendar calendar = Calendar.getInstance();

    public projectLogger() throws IOException {
    }

    public void error() throws IOException {
        setMessage("Error detected in code!");
        writeLogFile(getMessage(), new FileWriter("error.log"));

    }

    public void debug(String message) throws IOException {
        setMessage("Point \"Debug\"! in code: ".concat(message));
        writeLogFile(getMessage(), new FileWriter("debug.log"));
    }

    public void info(String message) throws IOException {
        setMessage("Info message: ".concat(message));
        writeLogFile(getMessage(), new FileWriter("info.log"));
    }

    private String createTime() {
        return " \"Time write log : \" \"".concat(String.valueOf(calendar.getTime())).concat("\n");
    }

    private void writeLogFile(String source, FileWriter fileWriter) throws IOException {
        source = source.concat(createTime());
        fileWriter.write(source);
        fileWriter.close();
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private String getMessage() {
        return message;
    }
}

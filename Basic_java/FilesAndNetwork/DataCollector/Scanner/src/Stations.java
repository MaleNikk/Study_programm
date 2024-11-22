import java.util.ArrayList;
import java.util.List;

public class Stations {
    private String name = "";
    private String depth = "";
    private String line = "";
    private String date = "";
    private boolean hasConnect = false;
    private final List<String> stations = new ArrayList<>();
    public void addListStations() {
        stations.add(toString());
    }

    @Override
    public String toString() {
        String printName = "".concat("\t\t\t\"name\": \"").concat(getName()).concat("\",\n");

        String printLine = "".concat("\t\t\t\"line\": \"").concat(getLine()).concat("\",\n");

        String printDate = "".concat("\t\t\t\"date\": \"").concat(getDate()).concat("\",\n");

        String printDepth = "".concat("\t\t\t\"depth\": \"").concat(getDepth()).concat("\",\n");

        String printHasConnect = "".concat("\t\t\t\"hasConnection\": \"").
                concat(String.valueOf(isHasConnect())).concat("\"\n");

        if (getDate().isEmpty()) {
            printDate = "";
        }

        if (getDepth().isEmpty() || getDepth().contains("?")) {
            printDepth = "";
        }

        if (!isHasConnect()) {
            printHasConnect = "";
            printDepth = "".concat("\t\t\t\"depth\": \"").concat(getDepth()).concat("\"\n");
        }

        if ((getDepth().isEmpty() || getDepth().contains("?")) && !isHasConnect()) {
            printDate = "".concat("\t\t\t\"date\": \"").concat(getDate()).concat("\"\n");
            printDepth = "";
            printHasConnect = "";
        }

        if (getDate().isEmpty() && (getDepth().isEmpty() || getDepth().contains("?")) && !isHasConnect()) {
            printLine = "".concat("\t\t\t\"line\": \"").concat(getLine()).concat("\"\n");
            printDate = "";
            printDepth = "";
            printHasConnect = "";
        }


        return "\n".
                concat("\t\t{\n").
                concat(printName).
                concat(printLine).
                concat(printDate).
                concat(printDepth).
                concat(printHasConnect).
                concat("\t\t}");
    }

    public List<String> getStations() {
        return stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isHasConnect() {
        return hasConnect;
    }

    public void setHasConnect(boolean hasConnect) {
        this.hasConnect = hasConnect;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

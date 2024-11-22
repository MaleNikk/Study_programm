package component;

import org.jetbrains.annotations.NotNull;
import testing.Test;
import java.util.HashSet;

public class Treatment {
    private final StorageAddress address;
    private final Connection connection;
    private final Test test;

    public Treatment(StorageAddress address, Connection connection, Test test) {
        this.address = address;
        this.connection = connection;
        this.test = test;
    }

    public void working() {
        for (String income : getConnection().loadPage()) {
            income = income.replaceAll("[*{']", "\"");
            if (getTest().checkHyperText(income)) {
                if (getTest().checkChar(income, ",")) {
                    String[] data = income.split(",");
                    for (String string : data) {
                        if (getTest().checkHyperText(string)) {
                            getListHyperLink().add(getHyperlink(string));
                        }
                    }
                } else {
                    getListHyperLink().add(getHyperlink(income));
                }
            }
        }
    }
    @NotNull
    private String getHyperlink(@NotNull String data) {
        String[] arrayFromData = data.split("http");
        String out;
        if (test.checkChar(arrayFromData[1], "\"")) {
            String[] strings = arrayFromData[1].split("\"", 2);
            out = "http".concat(strings[0]);
        } else {
            out = "http".concat(arrayFromData[1]);
        }
        return out;
    }

    public HashSet<String> getListHyperLink() {
        return this.address.getAddressesIncome();
    }

    private Connection getConnection() {
        return this.connection;
    }

    private Test getTest() {
        return this.test;
    }
}

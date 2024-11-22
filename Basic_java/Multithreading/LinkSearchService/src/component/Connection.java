package component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connection {
    private final String url;

    public Connection(String url) {
        System.out.println("Connection created.");
        this.url = url;
    }

    public List<String> loadPage() {
        List<String> dataIncome = new ArrayList<>();
        try {
            URL urlPage = new URL(getUrl());
            URLConnection connection = urlPage.openConnection();
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNext()) {
                String incomeData = scanner.next();
                dataIncome.add(incomeData);
            }
        } catch (IOException exception) {
            System.out.println("Invalid site link.");
            Thread.currentThread().interrupt();
        }
        return dataIncome;
    }

    public String getUrl() {
        return this.url;
    }
}

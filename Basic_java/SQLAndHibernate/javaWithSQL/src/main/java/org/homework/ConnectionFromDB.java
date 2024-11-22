package org.homework;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Getter
@Setter
public class ConnectionFromDB {
    private String url;
    private String name;
    private String key;
    public ConnectionFromDB(String url, String name, String key) {
        this.url = url;
        this.name = name;
        this.key = key;
    }
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(),getName(),getKey());
    }
    public void close() throws SQLException {
        createConnection().close();
    }
}

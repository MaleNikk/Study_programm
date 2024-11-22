package org.homework;

import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultSetFromDB {
    private ResultSet resultSet;
    private String queryString = "";
    private List<String> dataFromTable;
    public void addQuery(Statement statement, String query) throws SQLException {
        this.queryString = query;
        resultSet = statement.executeQuery(getQueryString());
    }
    public void resultStringQuery(String columnLabel1, String columnLabel) throws SQLException {
        dataFromTable = new ArrayList<>();
        setQueryString(getQueryString().concat(" * ").concat(columnLabel).concat(", ").concat(columnLabel1));
        while (getResultSet().next()) {
            String column = String.valueOf(getResultSet().getString(columnLabel));
            String column1 = String.valueOf(getResultSet().getString(columnLabel1));
            dataFromTable.add(column1.concat(" > ").concat(column));
        }
        dataFromTable.forEach(System.out::println);
    }
    public void resultIntQuery(int queryInt) throws SQLException {
        dataFromTable = new ArrayList<>();
        while (getResultSet().next()) {
            String column = getResultSet().getString(queryInt);
            String row = String.valueOf(getResultSet().getRow());
            getDataFromTable().add(row.concat(" - ").concat(column));
        }
        dataFromTable.forEach(System.out::println);
    }
}

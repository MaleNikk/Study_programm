package org.homework;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
@Getter
public class StatementFromDB {
    @Setter
    private Statement statement;
    public void createStatement(Connection connection) throws SQLException {
        setStatement(connection.createStatement());
    }
}

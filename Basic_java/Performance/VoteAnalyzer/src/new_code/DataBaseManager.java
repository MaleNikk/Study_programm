package new_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public record DataBaseManager(String dbName, String dbUser, String dbPass, String url) {

    public void init() throws SQLException {

        String startDb = "sys";
        String sqlDropDb = "DROP DATABASE IF EXISTS test_db;";
        String sqlCreateDb = "CREATE DATABASE test_db;";
        String sqlCreateTable = """
                CREATE TABLE voter_count (
                id INT NOT NULL AUTO_INCREMENT,
                name TEXT NOT NULL,
                surname TEXT NOT NULL,
                birthDate DATETIME NOT NULL,
                station_vote INT NOT NULL,
                date_vote DATETIME NOT NULL,
                count INT NOT NULL,
                PRIMARY KEY (id));
                """;

        Connection connection = connection(url().concat(startDb), dbUser(), dbPass());
        Statement statement = statement(connection);

        try {
            statement.execute(sqlDropDb);
            statement.execute(sqlCreateDb);
            connection.close();
            statement.close();
            connection = connection(url().concat(dbName()), dbUser(), dbPass());
            statement = statement(connection);
            statement.execute(sqlCreateTable);

        } catch (SQLException sqlException) {
            System.out.println(MessageFormat.format("We caught exception : {0}", sqlException));

        }
        statement.close();
        connection.close();
    }

    public void pushDataBase(String dataInsert) throws SQLException {
        Connection connection = connection(url().concat(dbName()), dbUser(), dbPass());
        Statement statement = statement(connection);
        String sqlInsert = builder().
                append("INSERT INTO voter_count (name, surname, birthDate, station_vote, date_vote,  count) VALUES ").
                append(dataInsert).toString();
        statement.executeUpdate(sqlInsert);
        statement.close();
        connection.close();
    }

    public StringBuilder builder() {
        return new StringBuilder();
    }

    public Connection connection(String url, String dbUser, String dbPass) throws SQLException {
        return DriverManager.getConnection(url, dbUser, dbPass);
    }

    public Statement statement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

}

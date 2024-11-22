package org.homework;

import java.sql.SQLException;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/skillbox";
    private static final String name = "root";
    private static final String key = "FhQ!grOuP:7391266";
    private static final StatementFromDB statementFromDB = new StatementFromDB();
    private static final ResultSetFromDB resultSetFromDB = new ResultSetFromDB();
    private static final StorageQuery storageQuery = new StorageQuery();
    private static final TreatmentDataFromDB treatment = new TreatmentDataFromDB();

    public static void main(String[] args) throws SQLException {

        System.out.println("""
                \tДля запуска программы требуется подключение к базе данных!
                \tВведите необходимые данные в соответствующие поля!
                """);

        ConnectionFromDB connectionFromDB = new ConnectionFromDB(url, name, key);

        statementFromDB.createStatement(connectionFromDB.createConnection());

        System.out.println("\nПросматриваем список баз данных:");
        viewDatabase("SHOW DATABASES");

        System.out.println("\nПросматриваем таблицы в базе данных skillbox: ");
        viewDatabase("SHOW TABLES");

        System.out.println("\nВыбираем необходимую таблицу и просматриваем поля таблицы: ");
        viewDatabase("DESCRIBE purchaselist");

        System.out.println("\nВыбираем необходимые данные из полей таблицы: ");
        selectData("SELECT * FROM purchaselist ");

        System.out.println("\nОбрабатываем полученные данные: ");
        treatment.workWithData(storageQuery.getQueryList(),storageQuery.getStorage());
        treatment.print();

        finishProgram(connectionFromDB, statementFromDB, resultSetFromDB);
    }
    public static void viewDatabase(String query) throws SQLException {
        resultSetFromDB.addQuery(statementFromDB.getStatement(), query);
        resultSetFromDB.resultIntQuery(1);
        storageQuery.addStorage(resultSetFromDB.getQueryString(), resultSetFromDB.getDataFromTable());

    }
    public static void selectData(String query) throws SQLException {
        resultSetFromDB.addQuery(statementFromDB.getStatement(), query);
        resultSetFromDB.resultStringQuery("course_name","subscription_date");
        storageQuery.addStorage(resultSetFromDB.getQueryString(), resultSetFromDB.getDataFromTable());
    }
    public static void finishProgram(ConnectionFromDB connection, StatementFromDB treatment, ResultSetFromDB resultSetFromDB)
            throws SQLException {
        connection.close();
        treatment.getStatement().close();
        resultSetFromDB.getResultSet().close();
    }
}

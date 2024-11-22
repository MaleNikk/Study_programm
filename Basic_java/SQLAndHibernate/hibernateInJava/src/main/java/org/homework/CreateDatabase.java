package org.homework;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter

public final class CreateDatabase {

    private String url,
                   name,
                   password,
                   nameDatabase,
                   countWritingPropertyFile,
                   fileUserInfo = "src/main/resources/userInfo.txt",
                   fileSqlScript = "src/main/resources/skillbox_sql_dump.sql";
    private String[] commands;
    public CreateDatabase() throws IOException { initial(); }
    public void initialUser() {
        print(
                """               
                В процессе выполнения программы автоматически будет выполнено:
                  - Формирование файла конфигурации.
                  - Создание базы данных.
                  - Создание таблиц в базе данных.
                  - Вывод всей необходимой аналитической информации в консоль.""");

        Scanner scanner = new Scanner(System.in);
        print("Введите путь к существующей базе данных(требуется только для соединения с СУБД):");
        setUrl(scanner.nextLine());
        print("Введите имя пользователя:");
        setName(scanner.nextLine());
        print("Введите пароль к базе данных:");
        setPassword(scanner.nextLine());
        print("Введите имя для новой базы данных");
        setNameDatabase(scanner.nextLine());
    }
    public void useUserInfo() throws IOException {
        setUrl(propertyFile(getFileUserInfo()).get(0));
        setName(propertyFile(getFileUserInfo()).get(1));
        setPassword(propertyFile(getFileUserInfo()).get(2));
        setNameDatabase(propertyFile(getFileUserInfo()).get(3));
        commands = new String[]{
                "DROP DATABASE IF EXISTS ".concat(getNameDatabase()).concat(";"),
                "CREATE database ".concat(getNameDatabase()).concat(";"),
                "SHOW databases;",
                "SHOW tables FROM ".concat(getNameDatabase()).concat(";")
        };
    }
    public void createDatabase() throws SQLException, IOException {
        useUserInfo();
        print("\nПодключаемся к MySQL и создаем базу данных:\n");
        print(String.valueOf(!statement().execute(getCommands()[0])));
        print(String.valueOf(!statement().execute(getCommands()[1])));
        showData(getCommands()[2]);
        close();
    }
    public void dropDatabase() throws SQLException, IOException {
        useUserInfo();
        print("Удаление базы данных!");
        print(String.valueOf(!statement().execute(getCommands()[0])));
    }

    private void showData(String query) throws SQLException, IOException {
        ResultSet resultSet = statement().executeQuery(query);
        print("\n".concat(query));
        while (resultSet.next()) {
            String result = resultSet.getString(1);
            print(result);
        }
        close();
    }
    private Connection connection() throws SQLException {
        return DriverManager.getConnection(getUrl(),getName(),getPassword());
    }
    private Statement statement() throws SQLException {
        return connection().createStatement();
    }
    private List<String> propertyFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    public void showTables() throws IOException {
        useUserInfo();
        try {
            showData(getCommands()[3]);
        } catch (SQLException e) {
            print(String.valueOf(e));
        }
    }
    private void initial() throws IOException {
        setCountWritingPropertyFile(propertyFile(getFileUserInfo()).get(4));
    }
    private void print(String message) {
        System.out.println(message);
    }
    private void close() throws SQLException, IOException {
        useUserInfo();
        statement().close();
        connection().close();
    }
}
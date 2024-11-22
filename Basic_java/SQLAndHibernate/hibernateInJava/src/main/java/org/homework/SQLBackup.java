package org.homework;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
public class SQLBackup {
    public SQLBackup() throws IOException {
        treatmentSqlDump();
        createDataCollectionFromSql();
    }
    private final List<String[]>
            listCourses = new ArrayList<>(),
            listStudents = new ArrayList<>(),
            listPurchase = new ArrayList<>(),
            listSubscriptions = new ArrayList<>(),
            listTeachers = new ArrayList<>();
    private final List<String> sqlInsert = new ArrayList<>();
    private void treatmentSqlDump() throws IOException {
        getSqlInsert().clear();
        List<String> allStrings = Files.readAllLines(Paths.get("src/main/resources/skillbox_sql_dump.sql"));
        for (String insert : allStrings) {
            if (insert.contains("INSERT INTO")) {
                getSqlInsert().add(insert);
            }
        }
    }
    private void createDataCollectionFromSql() {
        for (String data : getSqlInsert()) {
            if (data.contains("`Courses`")) {
                for (int i = 1; i < dataSqlPart1(data).length; i++) {
                    String[] dataOut = dataSqlPart2(dataSqlPart1(data)[i]);
                    getListCourses().add(dataOut);
                }
            }
            if (data.contains("`Students`")) {
                for (int i = 1; i < dataSqlPart1(data).length; i++) {
                    String[] dataOut = dataSqlPart2(dataSqlPart1(data)[i]);
                    getListStudents().add(dataOut);
                }
            }
            if (data.contains("`PurchaseList`")) {
                for (int i = 1; i < dataSqlPart1(data).length; i++) {
                    String[] dataOut = dataSqlPart2(dataSqlPart1(data)[i]);
                    getListPurchase().add(dataOut);
                }
            }
            if (data.contains("`Subscriptions`")) {
                for (int i = 1; i < dataSqlPart1(data).length; i++) {
                    String[] dataOut = dataSqlPart2(dataSqlPart1(data)[i]);
                    getListSubscriptions().add(dataOut);
                }
            }
            if (data.contains("`Teachers`")) {
                for (int i = 1; i < dataSqlPart1(data).length; i++) {
                    String[] dataOut = dataSqlPart2(dataSqlPart1(data)[i]);
                    getListTeachers().add(dataOut);
                }
            }
        }
    }

    public void createObjectsFromSqlFile(Session session) {
        print("\nLoad data to table from *.sql:\n");
                for (String[] dataOut:getListCourses()) {
                    Course course = new Course(
                            checkString(dataOut[4]),
                            checkInteger(dataOut[2]),
                            checkString(dataOut[1]),
                            checkInteger(dataOut[7]),
                            checkFloat(dataOut[8]),
                            checkInteger(dataOut[6]),
                            checkType(dataOut[3]),
                            checkInteger(dataOut[0]),
                            checkInteger(dataOut[5]));
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(course);
                    transaction.commit();
                }
                for (String[] dataOut:getListPurchase()){
                    PurchaseList purchaseList = new PurchaseList(
                            checkInteger(dataOut[2]),
                            checkDate(dataOut[3]),
                            checkString(dataOut[1]),
                            checkString(dataOut[0]));
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(purchaseList);
                    transaction.commit();
                }
                for (String[] dataOut:getListStudents()){
                    Student student = new Student(
                            checkInteger(dataOut[2]),
                            checkDate(dataOut[3]),
                            checkString(dataOut[1]),
                            checkInteger(dataOut[0]));
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(student);
                    transaction.commit();
                }

                for (String[] dataOut:getListSubscriptions()){
                    Subscription subscription = new Subscription(
                            checkDate(dataOut[2]),
                            checkInteger(dataOut[1]),
                            checkInteger(dataOut[0]));
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(subscription);
                    transaction.commit();
                }

                for (String[] dataOut:getListTeachers()){
                    Teacher teacher = new Teacher(
                            checkInteger(dataOut[3]),
                            checkInteger(dataOut[2]),
                            checkString(dataOut[1]),
                            checkInteger(dataOut[0]));
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(teacher);
                    transaction.commit();
                }

            for (String[] income:getListPurchase()) {
                LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList();
                for (String[] studentId:getListStudents()){
                    if (income[0].contains(studentId[1])) {
                        linkedPurchaseList.setStudentId(checkInteger(studentId[0]));
                    }
                }
                for (String[] courseId:getListCourses()){
                    if (income[1].contains(courseId[1])) {
                        linkedPurchaseList.setCourseId(checkInteger(courseId[0]));
                    }
                }
                Transaction transaction = session.beginTransaction();
                session.saveOrUpdate(linkedPurchaseList);
                transaction.commit();
            }
            print("Загрузка данных в таблицы завершена!");
    }

    private String[] dataSqlPart1(String income) {

        return income.replaceAll("[';]", "").replaceAll("[(]", "/").split("/");
    }

    private String[] dataSqlPart2(String income) {
        return income.replaceAll("[)']", "").replaceAll("[',]", "/").split("/");
    }

    private Integer checkInteger(String data) {
        return Objects.equals(data, "NULL") ? null : Integer.parseInt(data);
    }

    private String checkString(String data) {
        return Objects.equals(data, "NULL") ? "null" : data;
    }

    private LocalDateTime checkDate(String data) {
        return LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private TypeEducation checkType(String data) {
        return TypeEducation.valueOf(data);
    }

    private Float checkFloat(String data) {
        return Objects.equals(data, "NULL") ? null : Float.parseFloat(data);
    }

    private void print(String message) {
        System.out.println(message);
    }
}

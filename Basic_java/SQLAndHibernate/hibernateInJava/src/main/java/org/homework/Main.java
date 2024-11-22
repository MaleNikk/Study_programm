package org.homework;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

 /*
   Data for check:
   setUrl("jdbc:mysql://localhost:3306/mysql");
   setName("root");
   setPassword("FhQ!grOuP:7391266");
   setNameDatabase("test");
 */

public class Main {
    private static final CreateDatabase createDb;
    private static final SQLBackup sqlBackup;

    static {
        try {
            sqlBackup = new SQLBackup();
            createDb = new CreateDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String fileCfg = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            if (createDb.getCountWritingPropertyFile().contains("0")) {
                createDb.initialUser();
                config().createPropertyFile("1");
                print("Файлы конфигурации созданы, запустите приложение еще раз.");
            }
            if (createDb.getCountWritingPropertyFile().contains("1")) {
                createDb.useUserInfo();
                createDb.createDatabase();
                initialSessionFactory();
                createDb.showTables();
                print("Таблицы созданы, загружаем данные из файла *.sql :");
                Session session = sessionFactory.openSession();
                sqlBackup.createObjectsFromSqlFile(session);
                print("Получаем данные из таблиц в соответствии с заданием:");
                String hql = "FROM ".concat(LinkedPurchaseList.class.getSimpleName());
                List<LinkedPurchaseList> linkedPurchaseListList = session.createQuery(hql).getResultList();
                for (LinkedPurchaseList linkedPurchaseList:linkedPurchaseListList) {
                    print(String.valueOf(linkedPurchaseList));
                }
                sessionFactory.close();
                print("Введите 'return' для возвращения параметров системных файлов в исходное состояние ".
                        concat("и удаления созданной базы данных."));
                print("Введите 'close' для завершения без изменений.");
                String command = scanner.nextLine();
                if (command.contains("return")) {
                    returnInitialProperty();
                    print("Файлы конфигурации изменены на настройки по умолчанию! Программа завершена!");
                }
                if (command.contains("close")) {
                    print("Работа завершена.");
                }
            }
        } catch (Exception exception) {
            print("We caught exception! Check code and restart.".concat(String.valueOf(exception)));
        }
    }
    private static void initialSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure(fileCfg).build();
        Metadata metadata = new MetadataSources().getMetadataBuilder(registry).build();
        sessionFactory = metadata.buildSessionFactory();
    }
    private static void returnInitialProperty() throws IOException, SQLException {
        createDb.dropDatabase();
        HibernateConfig config = new HibernateConfig();
        config.createPropertyFile("0");
    }
    private static HibernateConfig config() {
        return new HibernateConfig(
                createDb.getUrl(), createDb.getName(), createDb.getPassword(), createDb.getNameDatabase());
    }
    private static void print(String message) {
        System.out.println(message);
    }
}

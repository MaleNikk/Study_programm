package new_code;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

public class Main {

    private final static String pathFile = "D:/skillbox/VoterAnalyzer/src/main/resources/file/test/data-18M.xml";
    private final static Integer limitInsert = 20000; //number of simultaneously loaded records into the database
    private static final String dbName = "test_db";
    private static final String dbUser = "root";
    private static final String dbPass = "FhQ!grOuP:7391266";

    private static final String url = "jdbc:mysql://localhost:3306/";

    public static void main(String[] args) {
        DataBaseManager manager = new DataBaseManager(dbName,dbUser,dbPass,url);
        ParserXml parserXml = new ParserXml();
        try {
            parserXml.parseXml(pathFile,manager,limitInsert);
        } catch (IOException | SQLException exception) {
            System.out.println(MessageFormat.format("We caught exception: {0}",exception));
        }
    }
}

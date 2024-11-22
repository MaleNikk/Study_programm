package new_code;


import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Scanner;

public class ParserXml {
    public void parseXml(String pathFile, DataBaseManager manager, Integer limitInsert) throws IOException, SQLException {
        long start = System.currentTimeMillis();

        manager.init();

        Scanner scanner = new Scanner(Path.of(pathFile));

        StringBuilder insert = new StringBuilder();

        StringBuilder insertData = new StringBuilder();

        int limitPush = limitInsert;

        while (scanner.hasNext()){

            String data = scanner.next();

            if (data.contains("name")){

                String[] dataSurname = data.split("\"");
                String[] dataName = scanner.next().split("\"");

                insert.append("'").append(dataName[0]).append("', '").append(dataSurname[1]).append("', '");

            }
            if (data.contains("birthDay")){

                String[] dataBirthDay = data.split("\"");

                insert.append(dataBirthDay[1]).append("', '");

            }
            if (data.contains("station")){

                String[] dataStation = data.split("\"");

                insert.append(dataStation[1]).append("', '");

            }
            if (data.contains("time")){

                String[] dataDateVote = data.split("\"");
                String[] dataTimeVote = scanner.next().split("\"");

                insert.append(dataDateVote[1]).append("T").append(dataTimeVote[0]).append("', '1'");

                if (insertData.isEmpty()){
                    insertData.append("(").append(insert).append(")");
                } else { insertData.append(", (").append(insert).append(")"); }

                limitPush--;

                if (limitPush == 0){
                    manager.pushDataBase(insertData.toString());
                    limitPush = limitInsert;
                    insertData = new StringBuilder();
                }
                insert = new StringBuilder();
            }
        }
        manager.pushDataBase(insertData.toString());
        long duration = System.currentTimeMillis() - start;
        System.out.println(MessageFormat.format("Time loading data: {0}",duration));
    }
}

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String folderPath =
            "D:\\homework\\java_basics\\FilesAndNetwork\\DataCollector\\data";
    private static final ReadFolder readFolder = new ReadFolder(folderPath);
    private static final DataFromCsv dataFromCsv = new DataFromCsv();
    private static final DataFromJson dataFromJson = new DataFromJson();
    private static final DataFromHtml dataFromHtml = new DataFromHtml();
    private static final DataMetroForJsonFile metro = new DataMetroForJsonFile();
    private static final projectLogger logger;
    private static final WriteJsonFile writeJsonFile;

    static {
        try {
            logger = new projectLogger();
            writeJsonFile = new WriteJsonFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ReadFile readFile = new ReadFile();
    private static final Stations stations = new Stations();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        logger.info("Start program.");
        readFolder.readFolder();
        while (true) {
            System.out.println("""
                    Enter command:
                     "1" - Read folder and print all paths folders and files.
                     "2" - Read file CSV, enjoy path from class ReadFolder.
                     "3" - Read file JSON, enjoy path from class ReadFolder.
                     "4" - Read file HTML, enjoy path from class ReadFolder.
                     "5" - Data from file HTML.
                     "6" - Data from file JSON.
                     "7" - Data from file CSV.
                     "8" - Collect all data and write JSON files..
                     "9" - Exit program.
                    """);
            String command = scanner.nextLine();
            if (command.equals("1")) {
                readFolder.printAllFolderPaths();
                readFolder.printAllFilesPaths();
                readFolder.printAllHtmlPaths();
                readFolder.printAllCsvPaths();
                readFolder.printAllJsonPaths();
            }
            if (command.equals("2")) {
                logger.info("Read file.");
                readFile.readCsv(readFolder.getListFilesCsv().get(0).getAbsolutePath());
                readFile.print();
            }
            if (command.equals("3")) {
                logger.info("Read file.");
                readFile.readJson(readFolder.getListFilesJson().get(0).getAbsolutePath());
                readFile.print();
            }
            if (command.equals("4")) {
                logger.info("Read file.");
                readFile.readHTML(readFolder.getListFilesHtml().get(0).getAbsolutePath());
                readFile.print();
            }
            if (command.equals("5")) {
                logger.debug("Create data from file.");
                createDataFromHtml();
                dataFromHtml.print();
            }
            if (command.equals("6")) {
                logger.debug("Create data from file.");
                createDataFromJson();
                dataFromJson.print();
            }
            if (command.equals("7")) {
                logger.debug("Create data from file.");
                createDataFromCsv();
                dataFromCsv.print();
            }
            if (command.equals("8")) {
                logger.error();
                createDataFromHtml();
                createDataFromJson();
                createDataFromCsv();
                collectAllData();
            }
            if (command.equals("9")) {
                System.out.println("Exit program! ");
                break;
            }
        }
    }
    public static void createDataFromCsv() throws IOException {
        for (int i = 0; i < 3; i++) {
            readFile.readCsv(readFolder.getListFilesCsv().get(i).getAbsolutePath());
            dataFromCsv.createDataFromFile(readFile.getStringsFromDataString());
        }
    }
    public static void createDataFromJson() throws IOException {
        for (int i = 0; i < 3; i++) {
            readFile.readCsv(readFolder.getListFilesJson().get(i).getAbsolutePath());
            dataFromJson.collection(readFile.getStringData(), '\"');
        }
    }
    public static void createDataFromHtml () throws IOException {
        readFile.readHTML(readFolder.getListFilesHtml().get(0).getAbsolutePath());
        dataFromHtml.collectLines(readFile.getStringsFromDataString());
        dataFromHtml.treatment(readFile.getStringData());
    }
    public static void collectAllData () {
        try {
            createDataFromCsv();
        } catch (IOException e) {
            try {
                logger.error();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        try {
            createDataFromJson();
        } catch (IOException e) {
            try {
                logger.error();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        try {
            createDataFromHtml();
        } catch (IOException e) {
            try {
                logger.error();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        for (String name:dataFromHtml.getAllStations()) {
            String[] checkName = name.split(":",2);
            String[] checkLine = name.split("=",2);
            String nameStation = checkName[0].trim();
            if (!(nameStation.isEmpty())) {
                stations.setDate("");
                if (dataFromCsv.getStations().containsKey(nameStation)) {
                    stations.setDate(dataFromCsv.getStations().get(nameStation));
                }
                stations.setDepth("");
                if (dataFromJson.getDataFromJsonFile().containsKey(nameStation)) {
                    stations.setDepth(dataFromJson.getDataFromJsonFile().get(nameStation));
                }
                stations.setLine(checkLine[1].trim());
                stations.setName(nameStation);
                stations.setHasConnect(dataFromHtml.hasConnect(name));
                stations.addListStations();
            }
        }
        metro.mapStationsInLines(dataFromHtml.getLinesWithListStations());
        metro.mapStationsWithTransition(dataFromHtml.getAllStations());
        metro.mapNameLinesMetro(dataFromHtml.getNameLinesMetro());
        try {
            writeJsonFile.writeFileStations("stations", stations.getStations(), "stations.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writeJsonFile.writeFileMap(metro.getStationsInLines(),metro.getStationTransition(),
                    metro.getNameLines(), "map.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

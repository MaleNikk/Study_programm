package management;

import component.Connection;
import component.StorageAddress;
import component.Treatment;
import saving.CreateDataFile;
import saving.WriterFiles;
import testing.Test;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ForkJoinPool;


public class Main {
    private static final String address = "https://www.tabnine.com";
    private static final String pathFile = "src/listLinks.txt";
    public static final Test test = new Test();
    public static final StorageAddress storage = new StorageAddress(new HashMap<>(),new LinkedHashSet<>());
    private static final Treatment start = new Treatment(storage, new Connection(address), test);

    public static void main(String[] args) {
        start.working();
        storage.getAddressesIncome().addAll(start.getListHyperLink());
        try (ForkJoinPool pool = new ForkJoinPool(3)) {
            pool.invoke(new Processing(start,storage));
        } catch (Exception exception){
            System.out.println("We caught exception.");
        } finally {
            System.out.println("Search complete.");
            System.out.println(storage.getLinks().size());
            writeData();
        }
    }
    public static void writeData(){
        CreateDataFile createDataFile = new CreateDataFile(storage);
        WriterFiles writer = new WriterFiles(createDataFile.createSource(),pathFile);
        writer.writeDataFile();
    }
}
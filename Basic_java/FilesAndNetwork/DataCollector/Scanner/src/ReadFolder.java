import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadFolder {
    private String path;

    private final List<File> listFilesJson = new ArrayList<>();

    private final List<File> listFilesCsv = new ArrayList<>();

    private final List<File> listFolders = new ArrayList<>();

    private final List<File> listAllFiles = new ArrayList<>();

    private final List<File> listFilesHtml = new ArrayList<>();
    public ReadFolder (String path) { this.path = path; }
    public void readFolder() {
        try {
            File[] listFiles = new File(getPath()).listFiles();
            if (listFiles != null) { checkFile(listFiles); }
        } catch (Exception exception) {
            System.out.println("We caught exception: incoming file is empty or the path is wrong!");
        }
    }

    private void checkFile(File[] file) {
        for (File files : file) {
            if (files.isDirectory()) {
                setPath(files.getAbsolutePath());
                listFolders.add(new File(getPath()));
                checkDirectory(files);
            } else {
                setPath(files.getAbsolutePath());
                add(getPath());
            }
        }

    }

    private void checkDirectory(File file) {
        if (file.isDirectory()) {
            checkFile(Objects.requireNonNull(file.listFiles()));
        } else {
            setPath(file.getAbsolutePath());
            add(getPath());
        }

    }

    private void add(String path) {
        setPath(path);
        listAllFiles.add(new File(getPath()));
        if ((getPath().substring((getPath().length() - 5))).equals(".json")) {
            listFilesJson.add(new File(getPath()));
        }
        if ((getPath().substring((getPath().length() - 4))).equals(".csv")) {
            listFilesCsv.add(new File(getPath()));
        }
        if ((getPath().substring((getPath().length() - 5))).equals(".html")) {
            listFilesHtml.add(new File(getPath()));
        }
    }

    private void printListPath(List<File> input, String nameMassive) {
        for (File file : input) {
            System.out.println(nameMassive + " - " + file);
        }
    }
    private void setPath(String path) {
        this.path = path;
    }
    public void printAllFolderPaths() {
        printListPath(getListFolders(), "Folder:");
    }
    public void printAllJsonPaths() {
        printListPath(getListFilesJson(), "File JSON : ");
    }
    public void printAllCsvPaths() {
        printListPath(getListFilesCsv(), "File CSV: ");
    }
    public void printAllHtmlPaths() {
        printListPath(getListFilesHtml(), "File HTML: ");
    }
    public void printAllFilesPaths () { printListPath(getListAllFiles(), "File : ");}

    public String getPath() { return path; }
    public List<File> getListFilesJson() { return listFilesJson; }
    public List<File> getListFilesCsv() { return listFilesCsv; }
    public List<File> getListFolders() { return listFolders; }
    public List<File> getListAllFiles() { return listAllFiles; }
    public List<File> getListFilesHtml() { return listFilesHtml; }
}

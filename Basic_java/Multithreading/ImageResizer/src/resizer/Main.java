package resizer;

import java.io.File;

public class Main {
    protected final static String startFolder = "workFolder/originalSize";
    protected final static File filesArray = new File(startFolder);
    protected final static File[] files = filesArray.listFiles();

    public static void main(String[] args) {

        assert files != null;

        File[] files1 = new File[part()];
        File[] files2 = new File[part()];
        File[] files3 = new File[part()];
        File[] files4 = new File[files.length - (files1.length + files2.length + files3.length)];

        for (int i = 0; i< files.length; i++){
            if (i<part()){
                files1[i] = files[i];
            }
            if (i>=part()&&i<(part()*2)){
                files2[i - part()] = files[i];
            }
            if (i>=(part()*2)&&i<(part()*3)){
                files3[i - (part()*2)] = files[i];
            }
            if (i>=(part()*3)){
                files4[i - (part()*3)] = files[i];
            }
        }

        ImageBuilder
                stream1 = new ImageBuilder(files1),
                stream2 = new ImageBuilder(files2),
                stream3 = new ImageBuilder(files3),
                stream4 = new ImageBuilder(files4);

        stream1.start();
        stream2.start();
        stream3.start();
        stream4.start();
    }
    private static Integer part(){
        assert files != null;
        int l = files.length;
        return l/4;
    }
}

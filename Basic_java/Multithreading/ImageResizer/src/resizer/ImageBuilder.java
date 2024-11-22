package resizer;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageBuilder extends Thread {
    private long start;
    private final File[] files;
    protected final String finishFolder = "workFolder/smallSize";
    protected final String finishFolderMaven = "workFolder/maven";
    protected final String finishFolderMaven2 = "workFolder/maven2";


    public ImageBuilder(File[] files) {
        this.files = files;
    }

    @Override
    public void run() {
        classicResizer();
        mavenResizer();
    }

    private void classicResizer(){
        setStart(System.currentTimeMillis());
        if (files == null) {
            System.out.println("We have no files in directory.");
        } else {
            try {
                for (File file : getFiles()) {

                    BufferedImage image = ImageIO.read(file);

                    int newWidth = 300;
                    int newHeight = (int) Math.round(
                            image.getHeight() / (image.getWidth() / (double) newWidth)
                    );
                    BufferedImage newImage = new BufferedImage(
                            newWidth, newHeight, BufferedImage.TYPE_INT_RGB
                    );

                    int widthStep = image.getWidth() / newWidth;
                    int heightStep = image.getHeight() / newHeight;

                    for (int x = 0; x < newWidth; x++) {
                        for (int y = 0; y < newHeight; y++) {
                            int rgb = image.getRGB(x * widthStep, y * heightStep);
                            newImage.setRGB(x, y, rgb);
                        }
                    }

                    File newFile = new File(finishFolder.concat("/").concat(file.getName()));
                    ImageIO.write(newImage, "jpg", newFile);
                }

            } catch (Exception exception) {
                System.out.println(exception);
                exception.printStackTrace();
            }
        }
        System.out.println("\nClassic resizer(only java code):\n".
                concat("Execution time: " ).
                concat(String.valueOf(System.currentTimeMillis() - getStart())));
    }

    public void mavenResizer(){
        setStart(System.currentTimeMillis());
        if (files == null) {
            System.out.println("We have no files in directory.");
        } else {
            try {
                for (File file : getFiles()) {

                    BufferedImage image = ImageIO.read(file);

                    int newWidth = 300;
                    int newHeight = (int) Math.round(
                            image.getHeight() / (image.getWidth() / (double) newWidth));

                    BufferedImage simpleResizeImage = Scalr.resize(image, newWidth);

                    BufferedImage newImage = Scalr.resize(image, Scalr.Method.AUTOMATIC,
                            Scalr.Mode.AUTOMATIC, newWidth, newHeight, Scalr.OP_ANTIALIAS);

                    File newFile = new File(finishFolderMaven.concat("/").concat(file.getName()));
                    ImageIO.write(newImage, "jpg", newFile);

                    File newFile2 = new File(finishFolderMaven2.concat("/").concat(file.getName()));
                    ImageIO.write(simpleResizeImage, "jpg", newFile2);
                }

            } catch (Exception exception) {
                System.out.println(exception);
                exception.printStackTrace();
            }
        }

        System.out.println("\nMaven Resizer(two methods):\n".
                concat("Execution time: " ).
                concat(String.valueOf(System.currentTimeMillis() - getStart())));
    }

    public File[] getFiles() { return files; }

    public long getStart() { return start; }

    public void setStart(long start) { this.start = start;  }
}

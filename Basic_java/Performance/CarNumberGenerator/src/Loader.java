import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Loader {

    public static void main(String[] args) throws Exception {
        System.out.println("Parent time for generate numbers.");
        Parent.parentPrimary();
        System.out.println("Child time for generate numbers.");
        Child.childPrimary();
        System.out.println("Multithreading.");
        Multithreading.runThreads();
    }

    public static class Parent {
        public static void parentPrimary() throws IOException {
            long start = System.currentTimeMillis();

            FileOutputStream writer = new FileOutputStream("res/parentNumbers.txt");

            char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
            int countNumbers = 1;

            for (int number = 1; number < 1000; number++) {
                int regionCode = 199;

                for (char firstLetter : letters) {
                    for (char secondLetter : letters) {
                        for (char thirdLetter : letters) {
                            String carNumber = firstLetter + padNumber(number, 3) +
                                    secondLetter + thirdLetter + padNumber(regionCode, 2);
                            writer.write(carNumber.getBytes());
                            writer.write('\n');
                            countNumbers++;
                        }
                    }
                }
            }

            writer.write(String.valueOf(countNumbers).getBytes());
            writer.flush();
            writer.close();

            System.out.println("Size file:".concat(String.valueOf(Files.size(Path.of("res/parentNumbers.txt")))));
            System.out.println((System.currentTimeMillis() - start) + " ms");
        }

        private static String padNumber(int number, int numberLength) {
            String numberStr = Integer.toString(number);
            int padSize = numberLength - numberStr.length();

            for (int i = 0; i < padSize; i++) {
                numberStr = '0' + numberStr;
            }

            return numberStr;
        }
    }

    public static class Child extends Thread {
        private final char[] letters;
        private final String path;
        public Child(char[] letters, String path) {
            this.letters = letters;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                generatorNumber(getLetters(),getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void childPrimary() throws IOException {
            generator(new char[]{'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'},"res/childNumbers.txt");
        }
        public static void generator(char[] letters, String path) throws IOException {
            long start = System.currentTimeMillis();

            char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

            StringBuilder carNumber;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            int countNumbers = 1;

            for (char a : letters) {
                for (char b : letters) {
                    for (char c : letters) {
                        for (char d: numbers) {
                            for (char f: numbers) {
                                for (char g : numbers) {
                                    carNumber = new StringBuilder();
                                    carNumber.
                                            append(a).
                                            append(d).
                                            append(f).
                                            append(g).
                                            append(b).
                                            append(c).
                                            append(numbers[1]).
                                            append(numbers[9]).
                                            append(numbers[9]).
                                            append('\n');
                                    countNumbers++;
                                    writer.write(carNumber.toString());
                                }
                            }
                        }
                    }
                }
            }
            writer.write(String.valueOf(countNumbers));
            writer.flush();
            writer.close();

            System.out.println("Size file:".concat(String.valueOf(Files.size(Path.of(path)))));
            System.out.println((System.currentTimeMillis() - start) + " ms");
        }
        public static void generatorNumber(char[] letters, String path) throws IOException {
            long start = System.currentTimeMillis();

            char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

            StringBuilder carNumber = new StringBuilder();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            int countNumbers = 1;

            for (char a : letters) {
                for (char b : letters) {
                    for (char c : letters) {
                        for (char d: numbers) {
                            for (char f: numbers) {
                                for (char g : numbers) {
                                    carNumber.
                                            append(a).
                                            append(d).
                                            append(f).
                                            append(g).
                                            append(b).
                                            append(c).
                                            append(numbers[1]).
                                            append(numbers[9]).
                                            append(numbers[9]).
                                            append('\n');
                                    countNumbers++;
                                }
                            }
                        }
                    }
                }
            }
            writer.write(carNumber.toString());
            writer.write(String.valueOf(countNumbers));
            writer.flush();
            writer.close();

            System.out.println("Size file:".concat(String.valueOf(Files.size(Path.of(path)))));
            System.out.println((System.currentTimeMillis() - start) + " ms");
        }

        public char[] getLetters() {
            return letters;
        }

        public String getPath() {
            return path;
        }
    }
    public static class Multithreading {

        private static final List<Child> tasks = new ArrayList<>();

        public static void runThreads(){
            createTasks();
            tasks.forEach(Thread::start);
        }
        private static void createTasks(){
            char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
            for (char a : letters) {
                tasks.add(new Child(new char[]{a},"res/multithreading/firstLetters".
                        concat(String.valueOf(a)).concat(".txt")));
            }
        }
    }
}



public class Concatenation {

    public static void main(String[] args) {
        print("Parent code time working.");
        parentCode();
        print("Child part one code time working.");
        childCodePartOne();
        print("Child code part two time working.");
        childCodePartTwo();
        print("Child code third part time working.");
        childCodePartThird();
    }
    public static void parentCode(){
        long start = System.currentTimeMillis();

        String str = "";
        for (int i = 0; i < 20_000; i++) {
            str += "some text some text some text";
        }
        print("Length: ".concat(String.valueOf(str.length())));
        print((System.currentTimeMillis() - start) + " ms");
    }
    public static void childCodePartOne() {
        long start = System.currentTimeMillis();

        String str = "";
        for (int i = 0; i < 20_000; i++) {
            str = str.concat("some text some text some text");
        }
        print("Length: ".concat(String.valueOf(str.length())));
        String difference = String.valueOf(System.currentTimeMillis() - start);
        print(difference.concat( " ms"));
    }
    public static void childCodePartTwo() {
        long start = System.currentTimeMillis();

        StringBuffer string = new StringBuffer();
        string.append("some text some text some text".repeat(20_000));
        print("Length: ".concat(String.valueOf(string.length())));
        String difference = String.valueOf(System.currentTimeMillis() - start);
        print(difference.concat( " ms"));
    }
    public static void childCodePartThird() {
        long start = System.currentTimeMillis();

        StringBuilder string = new StringBuilder();
        string.append("some text some text some text".repeat(20_000));
        print("Length: ".concat(String.valueOf(string.length())));
        String difference = String.valueOf(System.currentTimeMillis() - start);
        print(difference.concat( " ms"));
    }
    public static void print(String message) {
        System.out.println(message);
    }
}

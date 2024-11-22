public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        container.addCount(5672);
        System.out.println(container.getCount());

        // TODO: ниже напишите код для выполнения задания:
        //  С помощью цикла и преобразования чисел в символы найдите все коды
        //  букв русского алфавита — заглавных и строчных, в том числе буквы Ё.

        char j = 'А';
        int jCode;
        jCode = j;
        for (int b = jCode; b <= 65536; b++) {
            char s = (char) b;
            System.out.println(b + "-" + s);
            if (s == 'я') {
                System.out.println(b + "-" + s);
                break;
            }
        }
        for (int b = 0; b <= 65536; b++) {
            char s = (char) b;
            if (s == 'Ё') {
                System.out.println(b + "-" + s);
            }
            if (s == 'ё') {
                System.out.println(b + "-" + s);
                break;
            }
        }
    }
}

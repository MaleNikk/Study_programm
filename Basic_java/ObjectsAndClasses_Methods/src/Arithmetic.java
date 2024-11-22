import java.util.Random;

public class Arithmetic {
    int n;
    int b;
    Arithmetic() {
        System.out.println("Автоматическая генерация чисел.");
        n = new Random().nextInt(30);
        b = new Random().nextInt(30);
        System.out.println("Система подобрала первое число: " + n );
        System.out.println("Система подобрала второе число: " + b );
    }
    int volume() {
        return n*b;
    }
    int svolume() {
        return n+b;
    }
    public static void main(String[] args) {
        Arithmetic digit1 = new Arithmetic();
        int digit;
        digit = digit1.volume();
        System.out.println("Произведение чисел равно: " + digit);
        Arithmetic sdigit1 = new Arithmetic();
        int sdigit;
        sdigit = sdigit1.svolume();
        System.out.println("Сумма чисел равна: " + sdigit);
        if(sdigit>digit) {
            System.out.println("Наибольшее число " + sdigit);
            System.out.println("Наименьшее число " + digit);
            System.out.println("Число " + sdigit + " больше " + digit + " на " + (sdigit-digit));
        } else {
            System.out.println("Наибольшее число " + digit);
            System.out.println("Наименьшее число " + sdigit);
            System.out.println("Число " + digit + " больше " + sdigit + " на " + (digit-sdigit));
        }
    }
}

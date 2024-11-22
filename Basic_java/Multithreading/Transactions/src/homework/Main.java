package homework;

import component.Account;
import component.Bank;
import component.Transaction;
import java.util.*;

public class Main {
    private static final Bank bank = new Bank(new HashMap<>(), new Random());
    private static final LinkedList<Thread> treads = new LinkedList<>();

    public static void main(String[] args) {
        generateAccounts();
        startThreads();
    }
    private static void startThreads() {
        int countTreads = 0;
        do {
            countTreads++;
            Transaction transaction = new Transaction(bank, new Random());
            treads.add(transaction);
        } while (countTreads <= 25);
        treads.forEach(Thread::start);
    }
    private static void generateAccounts() {
        long money = 65000;
        int limitAccounts = 0;
        while (true) {
            limitAccounts++;
            if (limitAccounts != 1000) {
                Account account = new Account();
                account.setAccNumber(String.valueOf(limitAccounts));
                money = money * 6 / 5;
                money = (money > 300000) ? money * 3 / 4 : money * 8 / 7;
                account.setMoney(money);
                bank.getAccounts().put(account.getAccNumber(), account);
            } else {
                break;
            }
        }
    }
}

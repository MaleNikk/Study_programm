package component;

import java.util.Map;
import java.util.Random;

public class Bank {
    private final Map<String, Account> accounts;
    private final Random random;
    private long limit = 50000;

    public Bank(Map<String, Account> accounts, Random random) {
        this.accounts = accounts;
        this.random = random;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        long resizeBalanceFromAcc = getBalance(fromAccountNum) - amount;
        long resizeBalanceToAcc = getBalance(toAccountNum) + amount;
        if (transferIsBlocked(fromAccountNum,toAccountNum) && (resizeBalanceFromAcc > 0 && resizeBalanceToAcc > 0)) {
            synchronized (accounts.get(fromAccountNum)) {
                accounts.get(fromAccountNum).setMoney(resizeBalanceFromAcc);
            }
            synchronized (accounts.get(toAccountNum)) {
                accounts.get(toAccountNum).setMoney(resizeBalanceToAcc);
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        long sumAllAccounts = 0;
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            sumAllAccounts = sumAllAccounts + entry.getValue().getMoney();
        }
        return sumAllAccounts;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public String blockedTransferInfo(String fromAccountNum, String toAccountNum, long amount, String nameThread) {
        return ("\nTransaction blocked!".
                concat("\n\tTransfer info: ").
                concat("\n\tAmount: ").concat(String.valueOf(amount)).
                concat("\n\tFrom account number: ").concat(fromAccountNum).
                concat("\n\tTo account number: ").concat(toAccountNum).
                concat("\nDevelop info: ".concat(nameThread)));
    }

    public String transferInfo(String fromAccountNum, String toAccountNum, long amount, String nameThread) {
        return ("\nTransaction:".
                concat("\n\tTransfer info: ").
                concat("\n\tAmount: ").concat(String.valueOf(amount)).
                concat("\n\tFrom account number: ").concat(fromAccountNum).
                concat("\n\tMoney after transfer: ").concat(String.valueOf(getBalance(fromAccountNum))).
                concat("\n\tTo account number: ").concat(toAccountNum).
                concat("\n\tMoney after transfer: ").concat(String.valueOf(getBalance(toAccountNum))).
                concat("\nTransaction complete!").
                concat("\nDevelop info: ".concat(nameThread)));
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }
    public boolean transferIsBlocked(String fromAccNum, String toAccNum){
        return getAccounts().get(fromAccNum).isUnBlocked() && getAccounts().get(toAccNum).isUnBlocked();
    }
}

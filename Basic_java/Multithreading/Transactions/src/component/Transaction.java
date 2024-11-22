package component;

import java.util.Random;

public class Transaction extends Thread {
    private final Bank bank;
    private final Random random;

    public Transaction(Bank bank, Random random) {
        this.bank = bank;
        this.random = random;
    }

    @Override
    public void run() {
        int countTransactions = 0;
        do {
            countTransactions++;
            long amount = random.nextInt(45000, 65000);
            String fromAccountNum = String.valueOf(random.nextInt(1, 1000));
            String toAccountNum = String.valueOf(random.nextInt(1, 1000));
            if (amount <= bank.getLimit()) {
                crateTransaction(fromAccountNum, toAccountNum, amount);
            } else {
                try {
                    if (bank.isFraud(fromAccountNum, toAccountNum, amount)) {
                        print(bank.blockedTransferInfo(fromAccountNum, toAccountNum, amount, this.getName()));
                        bank.getAccounts().get(fromAccountNum).blocked();
                        bank.getAccounts().get(toAccountNum).blocked();
                    } else {
                        crateTransaction(fromAccountNum, toAccountNum, amount);
                    }
                } catch (InterruptedException e) {
                    print("\nWe caught exception, check code and restart.\n");
                }
            }
        } while (countTransactions <= 149);
    }

    private void crateTransaction(String fromAcc, String toAcc, long amount) {
        bank.transfer(fromAcc, toAcc, amount);
        print(bank.transferInfo(fromAcc, toAcc, amount, this.getName()));
    }

    private void print(String message) {
        System.out.println(message);
    }
}


package component;

public class Account {
    private boolean checkBlocked;
    private final boolean blocked;
    private final boolean unBlocked;
    private long money;
    private String accNumber;

    public Account(){
        this.blocked = false;
        this.unBlocked = true;
        this.checkBlocked = isUnBlocked();
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        if (isCheckBlocked()) {
            this.money = money;
        }
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isCheckBlocked() {
        return checkBlocked;
    }

    public void blocked() {
        System.out.println("Account blocked, please call to bank security!");
        this.checkBlocked = isBlocked();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isUnBlocked() {
        return unBlocked;
    }
}

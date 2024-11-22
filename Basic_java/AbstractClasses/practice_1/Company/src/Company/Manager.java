package Company;

public class Manager implements Employee {
    private double monthSalary;

    public void setMonthSalary(double inComeManager, double coefficient, double managerSalary) {

        monthSalary = managerSalary + (inComeManager * coefficient);
    }

    @Override
    public double getMonthSalary() {
        return monthSalary;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "monthSalary=" + (int) getMonthSalary() +
                '}';
    }
}

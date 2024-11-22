package Company;

public class TopManager implements Employee {
    private double monthSalary;

    public void setMonthSalary(double inCome, double topManagerSalary, double limitInCome, double coefficient) {

        if (inCome > limitInCome) {
            monthSalary = topManagerSalary + (topManagerSalary * coefficient);
        } else {
            monthSalary = topManagerSalary;
        }

    }

    @Override
    public double getMonthSalary() {
        return monthSalary;
    }

    @Override
    public String toString() {
        return "TopManager{" +
                "monthSalary=" + (int) getMonthSalary() +
                '}';
    }
}

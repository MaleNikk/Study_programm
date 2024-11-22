package Company;

public class Operator implements Employee {

    private double monthSalary;

    public void setMonthSalary(double operatorSalary) {

        monthSalary = operatorSalary;
    }

    @Override
    public double getMonthSalary() {
        return monthSalary;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "monthSalary=" + (int) getMonthSalary() +
                '}';
    }
}

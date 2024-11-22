package Company;

import java.util.*;

public class Company {
    protected Manager manager;
    protected TopManager topManager;
    protected Operator operator;
    protected int employee;
    private final ArrayList<Integer> topSalaryStaff = new ArrayList<>();
    private final ArrayList<Integer> lowestSalaryStaff = new ArrayList<>();
    private final ArrayList<Integer> salaries = new ArrayList<>();
    private final TreeMap<Integer, String> companyWorkers = new TreeMap<>();

    public Company(TopManager topManager, Manager manager, Operator operator) {
        this.topManager = topManager;
        this.manager = manager;
        this.operator = operator;

    }

    public TreeMap<Integer, String> getCompanyWorkers() {
        return companyWorkers;
    }

    protected ArrayList<Integer> getSalaries() {
        return salaries;
    }

    public void hire(String name) {
        employee++;
        if (name.equals("Топ - менеджер")) {
            salaries.add((int) (topManager.getMonthSalary()));
            companyWorkers.put(employee, topManager.toString());
        }
        if (name.equals("Менеджер")) {
            salaries.add((int) (manager.getMonthSalary()));
            companyWorkers.put(employee, manager.toString());
        }
        if (name.equals("Оператор")) {
            salaries.add((int) (operator.getMonthSalary()));
            companyWorkers.put(employee, operator.toString());
        }
        Collections.sort(salaries);
    }

    public void fire() {
        employee =0;
        companyWorkers.clear();
        salaries.clear();
    }

    public ArrayList<Integer> getLowestSalaryStaff(int count) {
        System.out.println("Наименьшие оплаты труда в компании: ");
        lowestSalaryStaff.clear();
        for (int a = 0; a < count; a++) {
            lowestSalaryStaff.add(getSalaries().get(a));
        }
        return lowestSalaryStaff;
    }

    public ArrayList<Integer> getTopSalaryStaff(int count) {
        System.out.println("Наибольшие оплаты труда в компании: ");
        topSalaryStaff.clear();
        Collections.reverse(salaries);
        for (int a = 0; a < count; a++) {
            topSalaryStaff.add(getSalaries().get(a));
        }
        return topSalaryStaff;
    }

    public void allCompany() {
        for (Map.Entry<Integer, String> entry : getCompanyWorkers().entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = entry.getValue();
            System.out.println("Сотрудник №: " + key + " - " + value);


        }
    }
}

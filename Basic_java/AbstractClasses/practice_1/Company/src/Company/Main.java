package Company;

import java.util.Random;
import java.util.Scanner;

public class Main {
    protected static TopManager topManager = new TopManager();
    protected static Manager manager = new Manager();
    protected static Operator operator = new Operator();
    protected static Company company = new Company(topManager, manager, operator);

    public static void main(String[] args) {
        int countOperators = 0, countManagers = 0, countTopManagers = 0, countSalaryDown, countSalaryUp;
        double nextCountTopManagers, nextCountManagers, nextCountOperators, dismissal;
        Random inCome = new Random();
        Random coefficient = new Random();
        double inComeCompany = inCome.nextInt(9_000_000, 39_000_000);
        double topManagerSalary = 250000, managerSalary = 90000, operatorSalary = 50000;
        double inComeManager = 100000 + (50000 * Math.random()), limitInCome = 10_000_000.0;
        double coefficientTopManager = 0.5, coefficientManager = 0.05, coefficientSalary;

        System.out.println("Симулятор найма сотрудников, расчета оплаты труда.");

        while (true) {
            String command;
            Scanner count = new Scanner(System.in);
            System.out.println("""
                    Введите команду управления организацией:
                    'HIRE' - найм сотрудников на работу,
                    'LIST SALARY' - список наибольших и наименьших заработных плат,
                    'ALL COMPANY' - полный список сотрудников и заработных плат,
                    'DISMISSAL' - увольнение сотрудников(процент),
                    'EXIT' - выход из программы.""");
            command = count.nextLine();
            if (command.equals("HIRE")) {
                System.out.println("Введите количество топ_менеджеров для найма: ");
                countTopManagers = count.nextInt();
                for (int a = countTopManagers; a > 0; a--) {
                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    topManager.setMonthSalary(inComeCompany, topManagerSalary * coefficientSalary,
                            limitInCome, coefficientTopManager);
                    company.hire("Топ - менеджер");
                }
                System.out.println("Введите количество менеджеров для найма: ");
                countManagers = count.nextInt();
                for (int a = countManagers; a > 0; a--) {

                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    manager.setMonthSalary(inComeManager, coefficientManager,
                            managerSalary * coefficientSalary);
                    company.hire("Менеджер");
                }
                System.out.println("Введите количество операторов для найма: ");
                countOperators = count.nextInt();
                for (int a = countOperators; a > 0; a--) {

                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    operator.setMonthSalary(operatorSalary * coefficientSalary);
                    company.hire("Оператор");
                }
            }
            if (command.equals("ALL COMPANY")) {
                System.out.println("Полный список сотрудников: ");
                company.allCompany();
            }
            if (command.equals("LIST SALARY")) {
                System.out.println("Введите требуемое количество наименьших оплат труда: ");
                countSalaryDown = count.nextInt();
                for (Integer salaryDown : company.getLowestSalaryStaff(countSalaryDown)) {
                    System.out.println(salaryDown);
                }
                System.out.println("Введите требуемое количество наибольших оплат труда: ");
                countSalaryUp = count.nextInt();
                for (Integer salaryUp : company.getTopSalaryStaff(countSalaryUp)) {
                    System.out.println(salaryUp);
                }
            }
            if (command.equals("DISMISSAL")) {
                System.out.println("Введите процент сокращения сотрудников:");
                company.fire();
                dismissal = 1.0 - count.nextDouble() / 100;
                nextCountTopManagers = countTopManagers * dismissal;
                nextCountManagers = countManagers * dismissal;
                nextCountOperators = countOperators * dismissal;
                for (int a = (int) nextCountTopManagers; a > 0; a--) {
                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    topManager.setMonthSalary(inComeCompany, topManagerSalary * coefficientSalary,
                            limitInCome, coefficientTopManager);
                    company.hire("Топ - менеджер");
                }
                for (int a = (int) nextCountManagers; a > 0; a--) {
                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    manager.setMonthSalary(inComeManager, coefficientManager,
                            managerSalary * coefficientSalary);
                    company.hire("Менеджер");
                }
                for (int a = (int) nextCountOperators; a > 0; a--) {
                    coefficientSalary = coefficient.nextDouble(0.85, 1.25);
                    operator.setMonthSalary(operatorSalary * coefficientSalary);
                    company.hire("Оператор");
                }
            }
            if (command.equals("EXIT")) {
                System.out.println("Программа завершена.");
                break;
            }
        }
    }
}
package com.example.skillboxsecondtask.testing;

import com.example.skillboxsecondtask.component.Student;

import java.util.Map;

public class TestDataIncome {
    public boolean checkText(String data) {
        return data.
                replaceAll("[a-z]", "").
                replaceAll("[A-Z]", "").
                replaceAll("[а-я]", "").
                replaceAll("[А-Я]", "").
                isEmpty();
    }

    public boolean checkDigit(int data) {
        boolean test = false;
        if (data >= 18 && data <= 60) {
            test = true;
        } else {
            System.out.println("At your age studying is prohibited by law!");
        }
        return test;
    }

    public boolean checkPresenceStudent(Student student, Map<Integer, Student> students) {
        boolean testStudent = false;
        for (Map.Entry<Integer, Student> entry : students.entrySet()) {
            if (entry.getValue().getFirstName().contains(student.getFirstName()) &&
                    entry.getValue().getLastName().contains(student.getLastName()) &&
                    entry.getValue().getAge() == student.getAge()) {
                testStudent = true;
                break;
            }
        }
        return testStudent;
    }
}

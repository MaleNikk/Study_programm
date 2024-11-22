package com.example.skillboxsecondtask.component;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Student {

    private final String firstName, lastName;
    private final int id, age;

    public Student(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        return "\nStudent:" +
                "\n\tid: " + getId() +
                "\n\tfirstName: " + getFirstName() +
                "\n\tlastName: " + getLastName() +
                "\n\tage: " + getAge();
    }
}

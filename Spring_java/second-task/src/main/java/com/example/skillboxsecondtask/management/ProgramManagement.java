package com.example.skillboxsecondtask.management;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ProgramManagement {
    private final Administration administration = new Administration();

    public ProgramManagement() {
        enjoy();
    }

    @ShellMethod
    public void enjoy() {
        print("""
                Welcome to program!
                Use command:
                'enjoy' - show all commands,
                'init' - create default students;
                'add <firstName lastName age>' - for add student(example: add misha zubkin 29),
                'remove <student id>' - for remove student(example: remove 1),
                'print' - for show added students,
                'delete' - for clear list students,
                'exit' - for exit program.
                """);
    }

    @ShellMethod
    public void add(String firstName, String lastName, int age) {
        administration.addStudent(firstName, lastName, age);
    }

    @ShellMethod
    public void remove(int id) {
        administration.removeStudent(id);
    }

    @ShellMethod
    public void print() {
        administration.printListStudents();
    }

    @ShellMethod
    public void delete() {
        administration.clear();
    }

    @ShellMethod
    public void init() {
        add("Misha", "Volkov", 25);
        add("Ksenia", "Smirnova", 23);
        add("Martine", "Fox", 31);
        add("Isabella", "Smith", 27);
        add("Paula", "Rogers", 35);
    }

    public void print(String message) {
        System.out.println(String.valueOf(message));
    }
}

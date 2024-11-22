package com.example.skillboxsecondtask.management;

import com.example.skillboxsecondtask.component.Student;
import com.example.skillboxsecondtask.component.StudentEventBuilder;
import com.example.skillboxsecondtask.storage.CollectionEvents;
import com.example.skillboxsecondtask.storage.CollectionStudents;
import com.example.skillboxsecondtask.testing.Logger;
import com.example.skillboxsecondtask.testing.TestDataIncome;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Administration {
    private final TestDataIncome test = new TestDataIncome();
    private final CollectionStudents collectionStudents = new CollectionStudents();
    private final CollectionEvents events = new CollectionEvents();
    private final Logger logger = new Logger();
    @Setter
    private Student student;
    @Setter
    private StudentEventBuilder eventBuilder;

    public void addStudent(String firstName, String lastName, int age) {
        logger.info("Student added.");
        if (test.checkText(firstName)) {
            print("\nTest first name successful.");
        } else {
            print("\nWrong first name! Please enjoy only words!");
            return;
        }
        if (test.checkText(lastName)) {
            print("\nTest last name successful.");
        } else {
            print("\nWrong last name! Please enjoy only words!");
            return;
        }
        if (test.checkDigit(age)) {
            print("\nTest age successful.");
        } else {
            print("\nYou won't be able to study at that age!");
            return;
        }
        int id = generateKey(getCollectionStudents().getStudents());
        setStudent(new Student(id, firstName, lastName, age));
        if (!test.checkPresenceStudent(getStudent(), getCollectionStudents().getStudents())) {
            getCollectionStudents().getStudents().put(id, getStudent());
            eventBuilder = new StudentEventBuilder(getEvents(),getStudent());
            getEventBuilder().addStudent();
        } else {
            print("\nStudent present!");
        }
    }

    public void removeStudent(int studentId) {
        logger.info("Student remove.");
        Student remove = getCollectionStudents().getStudents().get(studentId);
        getCollectionStudents().getStudents().remove(studentId, remove);
        eventBuilder = new StudentEventBuilder(getEvents(),remove);
        getEventBuilder().removeStudent();
    }

    public void printListStudents() {
        logger.info("Use print list.");
        printMap(getCollectionStudents().getStudents());
    }

    public void clear() {
        logger.info("Storage clean.");
        getCollectionStudents().getStudents().clear();
        print("Storage clear!");
    }

    private int generateKey(Map<Integer, ?> entryMap) {
        logger.debug("Generate key for new student.");
        int key = 0;
        if (entryMap.isEmpty()) {
            key++;
        } else {
            key = checkKey(entryMap);
        }
        return key;
    }

    private Integer checkKey(Map<Integer, ?> entryMap) {
        logger.debug("Check Map for generate key.");
        int checkKey = 0;
        int key = 1;
        for (Map.Entry<Integer, ?> entry : entryMap.entrySet()) {
            if (entry.getKey() >= key) {
                checkKey = entry.getKey();
            } else {
                key = 2;
            }
        }
        for (int a = 1; a <= checkKey; a++) {
            if (!entryMap.containsKey(a)) {
                key = a;
                break;
            } else {
                key = entryMap.size();
                key++;
            }

        }
        return key;
    }

    private void print(String message) {
        logger.info("Use System out println.");
        System.out.println(message); }

    private void printMap(Map<Integer, ?> entryMap) {
        logger.error("Print Map - storage students.");
        for (Map.Entry<Integer, ?> entry : entryMap.entrySet()) {
            print("\n".concat(String.valueOf(entry.getKey())));
            print("\t".concat(String.valueOf(entry.getValue())));

        }
    }
}

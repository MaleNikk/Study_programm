package org.homework;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import java.time.LocalDateTime;


@Entity
@Table(name = "Students")
@Getter
@Setter
@NoArgsConstructor

public class Student {
    public Student(Integer age, LocalDateTime registrationDate, String name,Integer studentId) {
        this.age = age;
        this.studentId = studentId;
        this.registrationDate = registrationDate;
        this.name = name;

    }
    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Id
    @NaturalId
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "\nStudent\n\t{" +
                "\n\t\tid = " + getStudentId() +
                ",\n\t\tname = " + getName() +
                ",\n\t\tage = " + getAge() +
                ",\n\t\tregistrationDate = " + getRegistrationDate() +
                "\n\t}\n";
    }
}

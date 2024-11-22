package org.homework;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;


@Entity
@Table(name = "Teachers")
@Getter
@Setter
@NoArgsConstructor

public class Teacher {
    public Teacher(Integer age, Integer salary, String name,  Integer teacher_id) {
        this.age = age;
        this.name = name;
        this.salary = salary;
        this.teacherId = teacher_id;
    }
    @Id
    @Column(name = "teacher_id")
    private Integer teacherId;
    @Id
    @NaturalId
    @Column(name = "name")
    private String name;
    @Column(name = "salary")
    private Integer salary;
    @Column(name = "age")
    private Integer age;

    @Override
    public String toString() {
        return "\nTeacher\n\t{" +
                "\n\t\tteacherId = " + getTeacherId() +
                ",\n\t\tname = " + getName() +
                ",\n\t\tsalary = " + getSalary() +
                ",\n\t\tage = " + getAge() +
                "\n\t}\n";
    }
}

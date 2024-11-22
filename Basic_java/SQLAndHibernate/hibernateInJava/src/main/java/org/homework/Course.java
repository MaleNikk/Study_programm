package org.homework;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "Courses")
@Getter
@Setter
@NoArgsConstructor

public class Course {
    public Course(String description, Integer duration, String name, Integer price, Float price_per_hour,
                  Integer students_count, TypeEducation type, Integer courseId, Integer teacherId) {
        this.description = description;
        this.duration = duration;
        this.name = name;
        this.price = price;
        this.pricePerHour = price_per_hour;
        this.studentsCount = students_count;
        this.teacherId = teacherId;
        this.type = type;
        this.courseId = courseId;
    }
    @Id
    @Column(name = "course_id")
    private Integer courseId;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "price")
    private Integer price;
    @Column(name = "price_per_hour")
    private Float pricePerHour;
    @Column(name = "students_count")
    private Integer studentsCount;
    @Id
    @Column(name = "teacher_id")
    private Integer teacherId;
    @Column(name = "description")
    private String description;
    @NaturalId
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeEducation type;
    @Override
    public String toString() {
        return "Course\n\t{" +
                "\n\t\tid = " + getCourseId() +
                ", \n\t\tname = " + getName() +
                ", \n\t\tduration = " + getDuration() +
                ", \n\t\ttype = " + getType() +
                ", \n\t\tdescription = " + getDescription() +
                ", \n\t\tteacher = " + getTeacherId() +
                ", \n\t\tstudentsCount = " + getStudentsCount() +
                ", \n\t\tprice = " + getPrice() +
                ", \n\t\tpricePerHour = " + getPricePerHour() +
                "\n\t}";
    }
}

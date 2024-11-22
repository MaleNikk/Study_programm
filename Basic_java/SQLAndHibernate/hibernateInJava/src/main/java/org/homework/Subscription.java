package org.homework;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Subscriptions")
@Getter
@Setter

public class Subscription {
    public Subscription(LocalDateTime subscriptionDate, Integer courseId, Integer studentId) {
        this.subscriptionDate = subscriptionDate;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @Id
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;


    @Override
    public String toString() {
        return "\nSubscriptions\n\t{" +
                ",\n\t\tstudentId = " + getStudentId() +
                ",\n\t\tcourseId = " + getCourseId() +
                ",\n\t\tsubscriptionDate = " + getSubscriptionDate() +
                "\n\t}\n";
    }
}

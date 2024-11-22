package org.homework;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import java.time.LocalDateTime;

@Entity
@Table(name = "PurchaseList")
@Getter
@Setter
@NoArgsConstructor

public class PurchaseList {
    public PurchaseList(Integer price, LocalDateTime subscriptionDate, String courseName, String studentName) {
        this.price = price;
        this.subscriptionDate = subscriptionDate;
        this.courseName = courseName;
        this.studentName = studentName;
    }
    @Id
    @NaturalId
    @Column(name = "student_name")
    private String studentName;

    @Id
    @NaturalId
    @Column(name = "course_name")
    private String courseName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    @Override
    public String toString() {
        return "\nPurchaseList\n\t{" +
                "\n\t\tstudentName = " + getStudentName() +
                ",\n\t\tcourseName = " + getCourseName() +
                ",\n\t\tprice = " + getPrice() +
                ",\n\t\tsubscriptionDate = " + getSubscriptionDate() +
                "\n\t}\n";
    }
}

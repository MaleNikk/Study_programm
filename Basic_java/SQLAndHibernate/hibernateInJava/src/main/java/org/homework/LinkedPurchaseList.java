package org.homework;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "linkedPurchaseList")
@Getter
@Setter
@NoArgsConstructor

public class LinkedPurchaseList {
    @Id
    @Column(name = "student_id", insertable = false, updatable = false)
    private Integer studentId;

    @Id
    @Column(name = "course_id", insertable = false, updatable = false)
    private Integer courseId;

    @Override
    public String toString() {
        return "\nLinkedPurchaseList\n\t{" +
                "\n\t\tstudentId = " + studentId +
                ",\n\t\tcourseId = " + courseId +
                "\n\t}\n";
    }
}

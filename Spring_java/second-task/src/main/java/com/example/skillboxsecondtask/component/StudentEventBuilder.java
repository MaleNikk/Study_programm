package com.example.skillboxsecondtask.component;

import com.example.skillboxsecondtask.storage.CollectionEvents;
import com.example.skillboxsecondtask.storage.CollectionStudents;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Getter
public class StudentEventBuilder {

    private final CollectionEvents collectionEvents;
    private final Student student;

    @EventListener(CollectionStudents.class)
    public void addStudent() {
        startEventAdd();
    }

    @EventListener(CollectionStudents.class)
    public void removeStudent() {
        startEventRemove();
    }

    private void startEventAdd() {
        Thread eventRemoveStudent = new Thread(() -> {
            StudentEvent studentEvent = StudentEvent.builder().
                    id(UUID.randomUUID()).
                    message("Student add: ".concat(getStudent().toString())).
                    dateTime(LocalDateTime.now()).
                    build();
            System.out.println(studentEvent.toString());
            getCollectionEvents().getEvents().add(studentEvent);
        });
        eventRemoveStudent.start();
    }

    private void startEventRemove() {
        Thread eventRemoveStudent = new Thread(() -> {
            StudentEvent studentEvent = StudentEvent.builder().
                    id(UUID.randomUUID()).
                    message("Student, id: ".concat(String.valueOf(getStudent().getId())).concat(" remove.")).
                    dateTime(LocalDateTime.now()).
                    build();
            System.out.println(studentEvent.toString());
            getCollectionEvents().getEvents().add(studentEvent);
        });
        eventRemoveStudent.start();
    }
}

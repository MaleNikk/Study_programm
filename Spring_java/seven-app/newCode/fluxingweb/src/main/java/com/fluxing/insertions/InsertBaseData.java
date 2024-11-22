package com.fluxing.insertions;

import com.fluxing.entity.Task;
import com.fluxing.entity.TaskStatus;
import com.fluxing.entity.User;
import com.fluxing.service.ServiceTask;
import com.fluxing.service.ServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class InsertBaseData {

    private final ServiceUser<User> serviceUser;

    private final ServiceTask<Task> serviceTask;


    @EventListener(ApplicationReadyEvent.class)
    public void startGenerate(){
        if (serviceUser.findAll().blockFirst() == null || serviceTask.findAll().blockFirst() == null){
            generateTasks(generateUsers());
        }
    }

    public Set<User> generateUsers(){
        int countUsers = 0;
        log.info("Init autogenerate data for users entities!");
        String name = "User_name_";
        String email = "User_email_";
        Set<User> users = new HashSet<>();
        do {
            countUsers++;
            String id = UUID.randomUUID().toString();
            Set<String> idTasks = Set.of(id,String.valueOf(countUsers++));
            User user = new User(id,name.concat(id),email.concat(id), idTasks);
            serviceUser.save(user).block();
            users.add(user);
        } while (countUsers <= 10);
        log.info("Init autogenerate data for users entities complete!");
        return users;
    }

    public void generateTasks(Set<User> users){
        int countTasks = 0;
        log.info("Init autogenerate data for tasks entities!");
        String name = "Task_name_";
        String description = "Task_description_";
        String authorId = "Author_id_";
        String assigneeId = "Assignee_id_";
        Instant createAt = Instant.now();
        Instant updateAt = Instant.now();
        do {
            String id = UUID.randomUUID().toString();
            Set<User> observers = Set.of(users.stream().findAny().get());
            serviceTask.save(new Task(id, name.concat(id), description.concat(id), authorId.concat(id),
                    assigneeId.concat(id), createAt, updateAt,TaskStatus.TODO,observers)).block();
            countTasks++;
        } while (countTasks <= 5);
        log.info("Init autogenerate data for tasks entities complete!");
    }
}

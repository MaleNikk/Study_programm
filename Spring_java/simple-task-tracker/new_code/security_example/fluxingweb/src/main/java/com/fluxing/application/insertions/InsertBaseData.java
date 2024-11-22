package com.fluxing.application.insertions;

import com.fluxing.application.entity.Task;
import com.fluxing.application.entity.TaskStatus;
import com.fluxing.application.entity.User;
import com.fluxing.application.service.ServiceTask;
import com.fluxing.application.service.ServiceUser;
import com.fluxing.security.dto.RoleType;
import com.fluxing.security.service.SecurityServiceJwt;
import com.fluxing.security.web.AdminRequest;
import com.fluxing.security.web.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class InsertBaseData {

    @Autowired
    private ServiceUser<User> serviceUser;

    @Autowired
    private ServiceTask<Task> serviceTask;

    @Autowired
    private SecurityServiceJwt securityServiceJwt;


    @EventListener(ApplicationStartedEvent.class)
    public void startGenerate() {
        if (serviceUser.findAll().blockFirst() == null || serviceTask.findAll().blockFirst() == null) {
            generateTasks(generateUsers());
            addAdminAndModerator();
        }
    }

    public List<User> generateUsers() {
        int countUsers = 1;
        log.info("Init autogenerate data for users entities!");
        String name = "User_name_";
        String email = "User_email_";
        String password = "User_";
        List<User> users = new ArrayList<>();
        while (countUsers <= 6) {
            String id = UUID.randomUUID().toString();
            Set<String> idTasks = Set.of(id, String.valueOf(countUsers));
            User user = new User(
                    id,
                    name.concat(String.valueOf(countUsers)),
                    email.concat(String.valueOf(countUsers)),
                    password.concat(String.valueOf(countUsers)),
                    Set.of(RoleType.USER),
                    idTasks);
            serviceUser.save(user).block();
            securityServiceJwt.register(
                    new UserRequest(user.getUsername(), user.getEmail(), user.getPassword()));
            users.add(user);
            countUsers++;

        }
        log.info("Created default users: {}", users);
        log.info("Init autogenerate data for users entities complete!");
        return users;
    }

    public void generateTasks(List<User> users) {
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
            Set<User> observers = Set.of(users.get(countTasks));

            serviceTask.save(new Task(id, name.concat(id), description.concat(id), authorId.concat(id),
                    assigneeId.concat(id), createAt, updateAt, TaskStatus.TODO, observers)).block();
            countTasks++;
        } while (countTasks <= 5);
        log.info("Init autogenerate data for tasks entities complete!");
    }

    public void addAdminAndModerator(){

        securityServiceJwt.register(new UserRequest(
                "admin",
                "admin",
                "admin"
        ),new AdminRequest(
                "admin",
                Set.of(RoleType.ADMIN),
                Set.of()
        ));

        securityServiceJwt.register(new UserRequest(
                "manager",
                "manager",
                "manager"
        ),new AdminRequest(
                "manager",
                Set.of(RoleType.MANAGER),
                Set.of()
        ));
    }
}

package com.fluxing.entity;

import com.fluxing.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Document(collection = "tasks")
public class Task {

    public Task(String id, String name, String description, String authorId, String assigneeId, Instant createdAt,
                Instant updatedAt, TaskStatus status, Set<User> observers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.authorId = authorId;
        this.assigneeId = assigneeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.observers = observers;
    }

    @Id
    private String id;
    private String name, description, authorId, assigneeId;
    private Instant createdAt, updatedAt;
    private TaskStatus status;

    @ReadOnlyProperty
    private Set<String> observerIds;

    @ReadOnlyProperty
    private User author, assignee;

    @Field("observers")
    private Set<User> observers;

    public static Task from(com.fluxing.model.TaskModel model) {
        log.info("Init method from(TaskModel) in class Task at system time: {}", System.currentTimeMillis()/1000);
        return model != null ?
                new Task(model.getId(), model.getName(), model.getDescription(), model.getAuthorId(),
                        model.getAssigneeId(), model.getCreatedAt(), model.getUpdatedAt(), model.getStatus(),
                        model.getObserverIds(), User.from(model.getAuthor()), User.from(model.getAssignee()),
                        apply(model.getObservers())) :
                new Task();
    }

    private static Set<User> apply(Set<UserModel> userModels) {
        log.info("Init method apply(Set<UerModel>) in class Task at system time: {}", System.currentTimeMillis()/1000);
        Set<User> users = new HashSet<>();
        userModels.forEach(userModel -> users.add(User.from(userModel)));
        return users;
    }
}

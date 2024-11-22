package com.fluxing.application.model;

import com.fluxing.application.entity.Task;
import com.fluxing.application.entity.TaskStatus;
import com.fluxing.application.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TaskModel {

    private String id;
    private String name, description, authorId, assigneeId;
    private Instant createdAt, updatedAt;
    private TaskStatus status;
    private Set<String> observerIds;
    private UserModel author, assignee;
    private Set<UserModel> observers;

    public TaskModel(String id, String description, String name, String authorId, String assigneeId, Instant createdAt,
                     Instant updatedAt, TaskStatus status, Set<UserModel> observers) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.authorId = authorId;
        this.assigneeId = assigneeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.observers = observers;
    }

    public static TaskModel from(Task taskModel) {
        log.info("Init method from in class TaskModel at system time: {}", System.currentTimeMillis() / 1000);
        return taskModel != null ?
                new TaskModel(taskModel.getId(), taskModel.getName(), taskModel.getDescription(), taskModel.getAuthorId(),
                        taskModel.getAssigneeId(), taskModel.getCreatedAt(), taskModel.getUpdatedAt(), taskModel.getStatus(),
                        taskModel.getObserverIds(), UserModel.from(taskModel.getAuthor()), UserModel.from(taskModel.getAssignee()),
                        apply(taskModel.getObservers())) :
                new TaskModel();
    }

    private static Set<UserModel> apply(Set<User> users) {
        Set<UserModel> userModels = new HashSet<>();
        users.forEach(user -> userModels.add(UserModel.from(user)));
        return userModels;
    }
}

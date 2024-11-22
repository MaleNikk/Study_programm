package com.fluxing.service;

import com.fluxing.entity.Task;
import com.fluxing.entity.TaskStatus;
import com.fluxing.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService implements ServiceTask<Task> {

    @Autowired
    private final TaskRepository taskRepository;

    @Override
    public Flux<Task> findAll() {
        log.info("Init method findAllTasks in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return taskRepository.findAll();
    }

    @Override
    public Mono<Task> findById(String id) {
        log.info("Init method findTaskById in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return taskRepository.findById(id);
    }

    @Override
    public Mono<Task> findByName(String name) {
        log.info("Init method findTaskByName in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return taskRepository.findByName(name);
    }

    @Override
    public Mono<Task> save(Task taskModel) {
        log.info("Init method saveTask in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        taskModel.setId(UUID.randomUUID().toString());
        return taskRepository.save(taskModel);
    }

    @Override
    public Mono<Task> update(String id, Task taskModel) {
        log.info("Init method updateTask in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return findById(id).flatMap(taskForUpdate -> {
            taskForUpdate.setName(taskModel.getName());
            taskForUpdate.setDescription(taskModel.getDescription());
            taskForUpdate.setAssigneeId(taskModel.getAssigneeId());
            taskForUpdate.setAuthorId(taskModel.getAuthorId());
            taskForUpdate.setCreatedAt(taskModel.getCreatedAt());
            taskForUpdate.setObservers(taskModel.getObservers());
            taskForUpdate.setAssignee(taskModel.getAssignee());
            taskForUpdate.setAuthor(taskModel.getAuthor());
            taskForUpdate.setObserverIds(taskModel.getObserverIds());
            taskForUpdate.setStatus(TaskStatus.IN_PROGRESS);
            taskForUpdate.setUpdatedAt(Instant.now());
            return taskRepository.save(taskForUpdate);
        });
    }

    @Override
    public Mono<Boolean> deleteById(String id) {
        log.info("Init method deleteTaskById in class TaskService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return taskRepository.deleteById(id).hasElement();
    }
}

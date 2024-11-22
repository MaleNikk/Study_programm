package com.fluxing.controller;

import com.fluxing.entity.Task;
import com.fluxing.entity.TaskStatus;
import com.fluxing.model.TaskModel;
import com.fluxing.publish.TaskPublisher;
import com.fluxing.service.ServiceTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webflux/test/task")
@Slf4j
public class TaskController {

    private ServiceTask<Task> taskService;


    private final TaskPublisher publishTask = new TaskPublisher();

    @GetMapping("/all")
    public Flux<TaskModel> getAllTasks() {
        log.info("Init method getAllTasks in controller class TaskController.");
        return taskService.findAll().map(TaskModel::from);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getTaskById(@PathVariable String id) {
        log.info("Init method getTaskById in controller class TaskController.");
        return taskService.findById(id)
                .map(TaskModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    public Mono<ResponseEntity<TaskModel>> getTaskByName(@RequestParam String name) {
        log.info("Init method getTaskByName in controller class TaskController.");
        return taskService.findByName(name)
                .map(TaskModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<TaskModel>> createTask(@RequestBody TaskModel model) {
        log.info("Init method createTask in controller class TaskController.");
        model.setStatus(TaskStatus.TODO);
        return taskService
                .save(Task.from(model))
                .map(TaskModel::from)
                .doOnSuccess(publishTask::publish)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<TaskModel>> updateTask(@PathVariable String id, @RequestBody TaskModel model) {
        log.info("Init method updateTask in controller class TaskController.");
        model.setStatus(TaskStatus.IN_PROGRESS);
        return taskService.update(id, Task.from(model))
                .map(TaskModel::from)
                .doOnSuccess(publishTask::publish)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable String id) {
        log.info("Init method deleteTaskById in controller class TaskController.");
        return taskService
                .findById(id)
                .map(TaskModel::from)
                .doOnSuccess(publishTask::publish)
                .flatMap(taskModel -> {
                    return taskService.deleteById(taskModel.getId());
                })
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TaskModel>> getTasksUpdates() {
        log.info("Init method getTasksUpdates in controller class TaskController.");
        return publishTask.getUpdatesSink()
                .asFlux()
                .map(taskModel -> ServerSentEvent.builder(taskModel)
                        .build());
    }
}

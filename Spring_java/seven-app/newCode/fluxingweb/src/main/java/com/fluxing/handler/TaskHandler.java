package com.fluxing.handler;

import com.fluxing.entity.Task;
import com.fluxing.service.ServiceTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.fluxing.handler.BoilerPlateCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskHandler implements HandlerTask {

    @Autowired
    private final ServiceTask<Task> taskService;

    private final String
            pathSave = "/webflux/test/task/add",
            pathUpdate = "/webflux/test/task/update/";

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("Init method get all tasks in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return getDataAllTasks(taskService);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        log.info("Init method findTaskById in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String id = request.pathVariable("id");
        return getDataTaskById(id, taskService);
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        log.info("Init method findTaskByName in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String name = request.pathVariable("name");
        return getDataTaskByName(name, taskService);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        log.info("Init method createTask in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return request.bodyToMono(Task.class)
                .flatMap(taskService::save)
                .flatMap(task -> {
                    return ServerResponse.created(URI.create(pathSave)).contentType(MediaType.APPLICATION_JSON).build();
                });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        log.info("Init method updateTask in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return taskService
                .findById(request.pathVariable("id"))
                .flatMap(task -> {
                    return taskService.update(task.getId(), request.bodyToMono(Task.class).block());
                })
                .flatMap(task -> {
                    return ServerResponse.created(URI.create(pathUpdate.concat(task.getId()))).build();
                });
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        log.info("Init method deleteTaskById in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String id = request.pathVariable("id");
        log.info("Task delete successful!");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(taskService.deleteById(id), Task.class);
    }

    @Override
    public Mono<ServerResponse> errorRequest(ServerRequest request) {
        log.info("Init method errorRequest in class TaskHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.error(new RuntimeException("Exception in error Request!")), String.class)
                .onErrorResume(exception -> {
                    log.error("Error in error Request", exception);
                    return ServerResponse.badRequest().body(Mono.error(exception), String.class);
                });
    }
}

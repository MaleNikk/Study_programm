package com.fluxing.application.handler;

import com.fluxing.application.entity.User;
import com.fluxing.application.service.ServiceUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.fluxing.application.handler.BoilerPlateCode.*;

@Component
@Slf4j
public class UserHandler implements HandlerUser {

    @Autowired
    private ServiceUser<User> userService;

    private final String
            pathUpdate = "/webflux/test/user/update/",
            pathSave = "/webflux/test/user/add";

    @Override
    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("Init method get all users in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return getDataAllUsers(userService);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        log.info("Init method findUserById in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String id = request.pathVariable("id");
        return getDataUserById(id, userService);
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        log.info("Init method findUserByName in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String name = request.pathVariable("name");
        return getDataUserByName(name, userService);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        log.info("Init method createUser in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return request.bodyToMono(User.class)
                .flatMap(userService::save)
                .flatMap(user -> {
                    return ServerResponse.created(URI.create(pathSave)).contentType(MediaType.APPLICATION_JSON).build();
                });
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        log.info("Init method updateUser in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return userService
                .findById(request.pathVariable("id"))
                .flatMap(task -> {
                    return userService.update(task.getId(), request.bodyToMono(User.class).block());
                })
                .flatMap(task -> {
                    return ServerResponse.created(URI.create(pathUpdate.concat(task.getId()))).build();
                });
    }

    @Override
    public Mono<ServerResponse> deleteById(ServerRequest request) {
        log.info("Init method deleteUserById in class UserHandler at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        String id = request.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.deleteById(id), User.class);
    }
}

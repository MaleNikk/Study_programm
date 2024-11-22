package com.fluxing.controller;

import com.fluxing.entity.User;
import com.fluxing.model.UserModel;
import com.fluxing.publish.UserPublisher;
import com.fluxing.service.ServiceUser;
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
@RequestMapping("/webflux/test/user")
@Slf4j
public class UserController {


    private ServiceUser<User> userService;

    private final UserPublisher publishUser = new UserPublisher();

    @GetMapping("/all")
    public Flux<UserModel> getAllUsers() {
        log.info("Init method getAllUsers in controller class UserController.");
        return userService.findAll().map(UserModel::from);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getUserById(@PathVariable String id) {
        log.info("Init method getUserById in controller class UserController.");
        return userService.findById(id)
                .map(UserModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    public Mono<ResponseEntity<UserModel>> getUserByName(@RequestParam String name) {
        log.info("Init method getUserByName in controller class UserController.");
        return userService.findByName(name)
                .map(UserModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel model) {
        log.info("Init method createUser in controller class UserController.");
        return userService
                .save(User.from(model))
                .map(UserModel::from)
                .doOnSuccess(publishUser::publish)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel model) {
        log.info("Init method updateUser in controller class UserController.");
        return userService.update(id, User.from(model))
                .map(UserModel::from)
                .doOnSuccess(publishUser::publish)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id) {
        log.info("Init method deleteUser in controller class UserController.");
        publishUser.publish(UserModel.from(userService.findById(id).block()));
        return userService
                .findById(id)
                .map(UserModel::from)
                .doOnSuccess(publishUser::publish)
                .flatMap(userModel -> {
                    return userService.deleteById(userModel.getId());
                })
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<UserModel>> getUsersUpdates() {
        log.info("Init method getUsersUpdates in controller class UserController.");
        return publishUser.getUpdatesSink()
                .asFlux()
                .map(userModel -> ServerSentEvent.builder(userModel).build());
    }
}

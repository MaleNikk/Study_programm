package com.fluxing.application.controller;

import com.fluxing.application.entity.User;
import com.fluxing.application.model.UserModel;
import com.fluxing.application.publish.UserPublisher;
import com.fluxing.application.service.ServiceUser;
import com.fluxing.security.service.SecurityServiceJwt;
import com.fluxing.security.web.AdminRequest;
import com.fluxing.security.web.ManagerRequest;
import com.fluxing.security.web.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/test/user")
@Slf4j
public class UserController {

    @Autowired
    private ServiceUser<User> userService;

    @Autowired
    private SecurityServiceJwt securityServiceJwt;

    @Autowired
    private UserPublisher publishUser;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Flux<UserModel> getAllUsers() {
        log.info("Init method getAllUsers in controller class UserController.");
        return userService.findAll().map(UserModel::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public Mono<ResponseEntity<UserModel>> getUserById(@PathVariable String id) {
        log.info("Init method getUserById in controller class UserController.");
        return userService.findById(id)
                .map(UserModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<UserModel>> getUserByName(@RequestParam String name) {
        log.info("Init method getUserByName in controller class UserController.");
        return userService.findByName(name)
                .map(UserModel::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel model) {
        log.info("Init method createUser in controller class UserController.");
        securityServiceJwt.register(new UserRequest(
                model.getName(),
                model.getEmail(),
                model.getPassword()
        ));
        return userService
                .save(User.from(model))
                .map(UserModel::from)
                .doOnSuccess(publishUser::publish)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel model) {
        log.info("Init method updateUser in controller class UserController.");
        if (model.getRoles() != null ){
            securityServiceJwt.updateRole(
                    new AdminRequest(
                            model.getName(),
                            model.getRoles(),
                            model.getIdTasks())
            );
        }
        if (model.getIdTasks() != null){
            securityServiceJwt.updateTasks(
                    new ManagerRequest(
                            model.getName(),
                            model.getIdTasks()
                            )
            );

        }
        return userService.update(id, User.from(model))
                .map(UserModel::from)
                .doOnSuccess(publishUser::publish)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id) {
        log.info("Init method deleteUser in controller class UserController.");
        User user = userService.findById(id).block();
        assert user != null;
        publishUser.publish(UserModel.from(user));
        securityServiceJwt.delete(user.getUsername());
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public Flux<ServerSentEvent<UserModel>> getUsersUpdates() {
        log.info("Init method getUsersUpdates in controller class UserController.");
        return publishUser.getUpdatesSink()
                .asFlux()
                .map(userModel -> ServerSentEvent.builder(userModel).build());
    }
}

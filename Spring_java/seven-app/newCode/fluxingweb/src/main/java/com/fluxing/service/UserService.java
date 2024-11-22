package com.fluxing.service;

import com.fluxing.entity.User;
import com.fluxing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ServiceUser<User> {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        log.info("Init method findAllUsers in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        log.info("Init method findUserById in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> findByName(String name) {
        log.info("Init method findUserByName in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return userRepository.findByName(name);
    }

    @Override
    public Mono<User> save(User user) {
        log.info("Init method saveUser in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Mono<User> update(String id, User user) {
        log.info("Init method updateUser in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return findById(id).flatMap(userForUpdate -> {
            userForUpdate.setName(user.getName());
            userForUpdate.setEmail(user.getEmail());
            userForUpdate.setIdTasks(user.getIdTasks());
            return userRepository.save(userForUpdate);
        });
    }

    @Override
    public Mono<Boolean> deleteById(String id) {
        log.info("Init method deleteUserById in class UserService at system time: {} sec.",
                System.currentTimeMillis() / 1000);
        return userRepository.deleteById(id).hasElement();
    }

}

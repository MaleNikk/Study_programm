package com.example.webfluxSecuriryExample.service;

import com.example.webfluxSecuriryExample.entity.AppUser;
import com.example.webfluxSecuriryExample.entity.AppUserRole;
import com.example.webfluxSecuriryExample.mapper.MappingEntity;
import com.example.webfluxSecuriryExample.model.UserModel;
import com.example.webfluxSecuriryExample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    private final MappingEntity userMapper;

    public UserService(
            @Autowired
            UserRepository repository,
            @Autowired
            PasswordEncoder encoder,
            @Autowired
            MappingEntity userMapper) {
        this.encoder = encoder;
        this.repository = repository;
        this.userMapper = userMapper;
    }

    public Mono<UserModel> registerUser(UserModel userModel) {
        log.info("Init register new user in UserService class.");
        AppUser appUser = getObject(userModel);
        appUser
                .toBuilder()
                .id(UUID.randomUUID().toString())
                .password(encoder.encode(userModel.getPassword()))
                .role(AppUserRole.USER.toString())
                .enable(true)
                .dateCreate(LocalDateTime.now())
                .dateUpdate(LocalDateTime.now())
                .build();

        return repository.save(appUser)
                .map(this::getObject)
                .doOnSuccess(T -> {
                    log.info("Register new user: {}", T);
                }).onErrorResume(throwable -> {
                    log.info("Checked error!!!");
                    return null;
                });
    }

    public Mono<UserModel> getUserById(String id) {
        return repository.findById(id).map(this::getObject);
    }

    public Mono<UserModel> getUserByName(String userName) {
        return repository.findByUserName(userName).map(this::getObject);
    }

    public AppUser getObject(UserModel userModel) {
        return userMapper.revert(userModel);
    }

    public UserModel getObject(AppUser appUser) {
        return userMapper.revert(appUser);
    }
}

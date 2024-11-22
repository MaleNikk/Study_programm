package com.fluxing.application.model;

import com.fluxing.application.entity.User;
import com.fluxing.security.dto.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserModel {

    private String id, name, email, password;
    private Set<RoleType> roles;
    private Set<String> idTasks;

    public static UserModel from(User user) {
        log.info("Init method from(User) in class Task at system time: {}", System.currentTimeMillis() / 1000);
        return user != null ?
                new UserModel(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles(),
                        user.getIdTasks()) :
                new UserModel();
    }
}

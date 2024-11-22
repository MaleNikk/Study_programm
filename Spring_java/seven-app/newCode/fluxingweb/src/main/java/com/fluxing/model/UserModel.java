package com.fluxing.model;

import com.fluxing.entity.User;
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

    private String id, name, email;
    private Set<String> idTasks;

    public static UserModel from(User user) {
        log.info("Init method from(User) in class Task at system time: {}", System.currentTimeMillis() / 1000);
        return user != null ?
                new UserModel(user.getId(), user.getName(), user.getEmail(), user.getIdTasks()) :
                new UserModel();
    }
}

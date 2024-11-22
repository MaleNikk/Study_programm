package com.fluxing.entity;

import com.fluxing.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    @Field("assignments")
    private Set<String> idTasks;

    public static User from(UserModel model) {
        log.info("Init method from(UserModel) in class User at system time: {}", System.currentTimeMillis()/1000);
        return model != null ?
                new User(model.getId(), model.getName(), model.getEmail(), model.getIdTasks()) :
                new User();
    }
}

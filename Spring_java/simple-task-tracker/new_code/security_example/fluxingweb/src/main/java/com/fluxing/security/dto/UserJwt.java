package com.fluxing.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;



@Document("user_jwt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJwt {
    @Id
    private Long id;

    private String username;

    private String email;

    private String password;

    @Field("roles")
    private Set<RoleType> roles;

    @Field("assignments")
    private Set<String> idTasks;
}

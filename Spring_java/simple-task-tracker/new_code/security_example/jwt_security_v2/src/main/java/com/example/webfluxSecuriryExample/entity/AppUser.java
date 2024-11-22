package com.example.webfluxSecuriryExample.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class AppUser {

    @Id
    private String id;

    private String userName;

    private String firstName;

    private String lastName;

    private String password;

    private String role;

    private LocalDateTime dateCreate;

    private LocalDateTime dateUpdate;

    private Boolean enable;

}

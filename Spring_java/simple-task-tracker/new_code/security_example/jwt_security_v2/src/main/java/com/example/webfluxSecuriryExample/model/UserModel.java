package com.example.webfluxSecuriryExample.model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    private String id;

    private String userName,firstName, lastName, password, role;

    private LocalDateTime dateCreate, dateUpdate;

    private Boolean enable;
}

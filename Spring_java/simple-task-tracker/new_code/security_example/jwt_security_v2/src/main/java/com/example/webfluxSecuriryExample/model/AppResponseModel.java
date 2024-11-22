package com.example.webfluxSecuriryExample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseModel {

    private Long userId;

    private String token;

    private Date issueAt, expireAt;
}

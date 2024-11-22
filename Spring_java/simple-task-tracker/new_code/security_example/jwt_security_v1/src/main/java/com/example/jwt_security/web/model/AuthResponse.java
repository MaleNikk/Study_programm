package com.example.jwt_security.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class AuthResponse {

    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;
}

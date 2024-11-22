package com.example.jwt_security.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RefreshTokenResponse {

    private String accessToken;

    private String refreshToken;

}

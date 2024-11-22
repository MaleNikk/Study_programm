package com.example.jwt_security.web.controller;

import com.example.jwt_security.exception.AlreadyExistException;
import com.example.jwt_security.repository.UserRepository;
import com.example.jwt_security.security.SecurityService;
import com.example.jwt_security.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/auth")
@RequiredArgsConstructor
public class AppAuthController {

    private final UserRepository userRepository;

    private final SecurityService securityService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistException("Username already exists!");
        }
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistException("Email already exists!");
        }

        securityService.register(createUserRequest);

        return ResponseEntity.ok(new SimpleResponse("User created!"));

    }

    @PostMapping("/refresh_token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest tokenRequest) {
        return ResponseEntity.ok(securityService.tokenResponse(tokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();

        return ResponseEntity.ok(new SimpleResponse("User logout. Username is: " + userDetails.getUsername()));
    }
}

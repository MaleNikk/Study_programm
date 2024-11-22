package com.fluxing.security.controller;

import com.fluxing.security.exception.AlreadyExistsException;
import com.fluxing.security.repository.UserRepositoryJwt;
import com.fluxing.security.service.SecurityServiceJwt;
import com.fluxing.security.web.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping("/webflux/test/auth")
public class ApplicationAuthController {

    @Autowired
    private UserRepositoryJwt repositoryJwt;

    @Autowired
    private SecurityServiceJwt serviceJwt;

    private final Logger log = LoggerFactory.getLogger(ApplicationAuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authUser (@RequestBody LogInRequest logInRequest) {
        log.info("Init authentication user!");
        return ResponseEntity.ok(serviceJwt.authenticateUser(logInRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponseJwt> registerUser(@RequestBody UserRequest userRequest) {
        log.info("Init register user!");
        if (repositoryJwt.existsByUsername(userRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (repositoryJwt.existsByEmail(userRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        serviceJwt.register(userRequest);

        return ResponseEntity.ok(new SimpleResponseJwt("User crated!"));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Init get refresh token!");
        return ResponseEntity.ok(serviceJwt.refreshToken(request));
    }

    @PostMapping("/set_roles")
    public ResponseEntity<SimpleResponseJwt> setRoles(@RequestBody AdminRequest adminRequest) {
        log.info("Init set role user!");
        serviceJwt.updateRole(adminRequest);
        return ResponseEntity.ok(new SimpleResponseJwt("Roles update!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponseJwt> logout(@AuthenticationPrincipal UserDetails userDetails) {

        serviceJwt.logout();

        return ResponseEntity.ok(
                new SimpleResponseJwt(MessageFormat.format("User: {0} logout", userDetails.getUsername())));
    }

}

package com.fluxing.security.service;


import com.fluxing.security.configuration.application.ApplicationJwtUtils;
import com.fluxing.security.configuration.security.AppUserDetailsJwt;
import com.fluxing.security.dto.RefreshTokenJwt;
import com.fluxing.security.dto.RoleType;
import com.fluxing.security.dto.UserJwt;
import com.fluxing.security.exception.RefreshTokenException;
import com.fluxing.security.repository.UserRepositoryJwt;
import com.fluxing.security.web.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class SecurityServiceJwt {

    private final Logger log = LoggerFactory.getLogger(SecurityServiceJwt.class);

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private ApplicationJwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService tokenService;

    @Autowired
    private UserRepositoryJwt userRepositoryJwt;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LogInRequest logInRequest) {
        Authentication authentication = manager
                .authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInRequest.getUsername(),
                        logInRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetailsJwt userDetailsJwt = (AppUserDetailsJwt) authentication.getPrincipal();

        List<String> roles = userDetailsJwt
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshTokenJwt tokenJwt = tokenService.createToken(userDetailsJwt.getUsername());

        return  AuthResponse
                .builder()
                .token(jwtUtils.generateJwtToken(userDetailsJwt))
                .refreshToken(tokenJwt.getToken())
                .username(userDetailsJwt.getUsername())
                .email(userDetailsJwt.getEmail())
                .roles(roles)
                .build();
    }

    public void register(UserRequest userRequest) {

        log.info("Init register user entity at system time: {}", System.currentTimeMillis());

        UserJwt user = new UserJwt(
                (long) UUID.randomUUID().toString().hashCode(),
                userRequest.getUsername(),
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword()),
                Set.of(RoleType.USER),
                Set.of());

        userRepositoryJwt.save(user);
    }

    public void register(UserRequest userRequest, AdminRequest adminRequest) {

        log.info("Init register admin entity at system time: {}", System.currentTimeMillis());

        UserJwt user = new UserJwt(
                (long) UUID.randomUUID().toString().hashCode(),
                userRequest.getUsername(),
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword()),
                adminRequest.getRoles(),
                Set.of());

        userRepositoryJwt.save(user);
    }

    public void updateRole(AdminRequest adminRequest){

        log.info("Init update user entity at system time: {}", System.currentTimeMillis());

        UserJwt userJwt = userRepositoryJwt.findByUsername(adminRequest.getUsername()).orElse(new UserJwt());
        if (!adminRequest.getRoles().isEmpty() && !userJwt.getUsername().isEmpty()) {
            userRepositoryJwt.delete(userJwt);
            userRepositoryJwt.save(
                    new UserJwt(
                            userJwt.getId(),
                            adminRequest.getUsername(),
                            userJwt.getEmail(),
                            userJwt.getPassword(),
                            adminRequest.getRoles(),
                            adminRequest.getIdTasks()));
        }

    }

    public void updateTasks(ManagerRequest managerRequest){

        log.info("Init update tasks user at system time: {}", System.currentTimeMillis());

        UserJwt userJwt = userRepositoryJwt.findByUsername(managerRequest.getUsername()).orElse(new UserJwt());
        if (userJwt.getUsername().equals(managerRequest.getUsername())) {
            userRepositoryJwt.delete(userJwt);
            userRepositoryJwt.save(
                    new UserJwt(
                            userJwt.getId(),
                            managerRequest.getUsername(),
                            userJwt.getEmail(),
                            userJwt.getPassword(),
                            userJwt.getRoles(),
                            managerRequest.getIdTasks()));
        }

    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        log.info("Init generate refreshToken at system time: {}", System.currentTimeMillis());

        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return tokenService.findByRefreshToken(requestRefreshToken)
                .map(tokenService::checkRefreshToken)
                .map(RefreshTokenJwt::getUsername)
                .map(username -> {

                    if (userRepositoryJwt.existsByUsername(username)) {

                        String token = jwtUtils.generateTokenFromUsername(username);

                        return new RefreshTokenResponse(token, tokenService.createToken(username).getToken());
                    }
                    throw new RefreshTokenException("Error trying to get token for username.");
                }).orElseThrow(() ->
                        new RefreshTokenException("Refresh token not found", refreshTokenRequest.toString()));

    }

    public void logout() {

        log.info("Init logout at system time: {}", System.currentTimeMillis());

        Object currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentPrincipal instanceof AppUserDetailsJwt userDetailsJwt) {

       String userId = userDetailsJwt.getUsername();

       tokenService.deleteByUserId(userId);

        }

    }

    public void delete(String username){
        log.info("Init delete at system time: {}", System.currentTimeMillis());
        UserJwt userJwt = userRepositoryJwt.findByUsername(username).orElse(new UserJwt());
        if (userJwt.getUsername().equals(username)) {
            userRepositoryJwt.delete(userJwt);
        }
    }

}

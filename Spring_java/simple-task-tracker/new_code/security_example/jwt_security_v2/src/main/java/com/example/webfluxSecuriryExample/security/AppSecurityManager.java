package com.example.webfluxSecuriryExample.security;

import com.example.webfluxSecuriryExample.entity.AppUser;
import com.example.webfluxSecuriryExample.exeption.AppVerifyException;
import com.example.webfluxSecuriryExample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppSecurityManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        AppDataPrincipal principal = (AppDataPrincipal) authentication.getPrincipal();
        return userService.getUserById(String.valueOf(principal.getId()))
                .map(userService::getObject)
                .filter(AppUser::getEnable)
                .switchIfEmpty(Mono.error(new AppVerifyException("User disabled")))
                .map(user -> authentication);
    }
}

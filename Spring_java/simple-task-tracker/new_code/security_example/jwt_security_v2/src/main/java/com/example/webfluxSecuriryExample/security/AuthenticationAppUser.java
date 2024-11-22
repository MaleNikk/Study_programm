package com.example.webfluxSecuriryExample.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthenticationAppUser {

    public static Mono<Authentication> create(AppSecurityHandler.VerificationData verificationData) {
        Claims claims = verificationData.claims;
        String subject = claims.getSubject();
        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        long principalId = Long.parseLong(subject);
        AppDataPrincipal principal = new AppDataPrincipal(principalId,username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}

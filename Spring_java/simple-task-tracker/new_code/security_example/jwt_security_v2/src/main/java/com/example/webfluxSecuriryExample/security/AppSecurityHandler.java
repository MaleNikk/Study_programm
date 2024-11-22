package com.example.webfluxSecuriryExample.security;

import com.example.webfluxSecuriryExample.exeption.AppAuthException;
import com.example.webfluxSecuriryExample.exeption.AppVerifyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

public class AppSecurityHandler {

    private final String secret;

    public AppSecurityHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationData> check(String accessToken) {

        return Mono.just(verify(accessToken))
                .onErrorResume(exception ->Mono.error(new AppVerifyException(exception.getMessage())));
    }

    private VerificationData verify(String token) {
        Claims claims = getClaims(token);

        final Date expirationDate = claims.getExpiration();

        if (expirationDate.before(new Date())) {
            throw new AppAuthException("Token expired", "TIME_EXPIRED_BAD");
        }

        return new VerificationData(claims,token);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public static class VerificationData {

        public Claims claims;

        public String token;

        public VerificationData(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}

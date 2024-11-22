package com.example.webfluxSecuriryExample.security;

import com.example.webfluxSecuriryExample.entity.AppUser;
import com.example.webfluxSecuriryExample.exeption.AppAuthException;
import com.example.webfluxSecuriryExample.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AppSecurityService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final String secret;

    private final Integer expiration;

    private final String issuer;

    public AppSecurityService(
            @Autowired
            UserService userService,
            @Autowired
            PasswordEncoder passwordEncoder,
            @Value("${jjwt.secret}")
            String secret,
            @Value("${jjwt.expiration}")
            Integer expiration,
            @Value("${jjwt.issuer}")
            String issuer) {
        log.info("Init AppSecurityService");
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.secret = secret;
        this.expiration = expiration;
        this.issuer = issuer;
    }

    public Mono<AppTokenDetails> authenticate(String username, String password) {
        return userService.getUserByName(username)
                .flatMap(user -> {
                    return ((user != null) && (passwordEncoder.matches(password, user.getPassword()))) ?
                            Mono.just(generateToken(userService.getObject(user)).toBuilder().userId(Long.valueOf(user.getId())).build())
                            : Mono.error(new AppAuthException("Authentication not valid", "AUTHENTICATION_ERROR"));
                })
                .switchIfEmpty(Mono.error(new AppAuthException("Account is not present", "ACCOUNT_NOT_PRESENT")));
    }

    private AppTokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {

        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return AppTokenDetails.builder()
                .token(token)
                .issueAt(createdDate)
                .expireAt(expirationDate)
                .build();
    }

    private AppTokenDetails generateToken(Map<String, Object> claims, String subject) {

        long expirationTimeToMillis = expiration * 1000L;

        Date expirationDate = new Date(new Date().getTime() + expirationTimeToMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private AppTokenDetails generateToken(AppUser appUser) {
        return generateToken(Map.of("role", appUser.getRole(), "username", appUser.getUserName()),
                appUser.getId());
    }
}

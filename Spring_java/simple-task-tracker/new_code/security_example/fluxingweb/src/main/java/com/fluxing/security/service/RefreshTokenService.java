package com.fluxing.security.service;

import com.fluxing.security.dto.RefreshTokenJwt;
import com.fluxing.security.exception.RefreshTokenException;
import com.fluxing.security.repository.RefreshTokenRepositoryJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    @Autowired
    private RefreshTokenRepositoryJwt tokenRepositoryJwt;

    public Optional<RefreshTokenJwt> findByRefreshToken(String token){
        return tokenRepositoryJwt.findByToken(token);
    }

    public RefreshTokenJwt createToken(String username){
        RefreshTokenJwt tokenJwt = RefreshTokenJwt
                .builder()
                .username(username)
                .expireTime(Instant.now().plusMillis(tokenExpiration.toMillis()))
                .token(UUID.randomUUID().toString())
                .build();
        return tokenRepositoryJwt.save(tokenJwt);
    }

    public RefreshTokenJwt checkRefreshToken(RefreshTokenJwt tokenJwt) {
        log.info("Init check refresh token at system time: {} !", System.currentTimeMillis());
        if ((tokenJwt.getExpireTime().isBefore(Instant.now()))) {
            tokenRepositoryJwt.delete(tokenJwt);
            throw new RefreshTokenException("Refresh token was expired. Repeat login");
        }

        return tokenJwt;
    }

    public void deleteByUserId(String userId) {
        log.info("Init delete refresh token by user id!");
        tokenRepositoryJwt.deleteByUserId(userId);
    }

}

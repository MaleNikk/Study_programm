package com.example.jwt_security.service;

import com.example.jwt_security.entity.RefreshToken;
import com.example.jwt_security.exception.RefreshTokenException;
import com.example.jwt_security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository tokenRepository;

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                .token(UUID.randomUUID().toString())
                .build();
        return tokenRepository.save(refreshToken);
    }

    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Repeat sigin action!");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }
}

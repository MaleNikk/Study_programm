package com.example.jwt_security.security.events;

import com.example.jwt_security.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppRedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpireEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken expireRefreshToken = (RefreshToken) event.getValue();

        if (expireRefreshToken == null) {
            throw new RuntimeException("Refresh token is null in handleRedisKeyExpireEvent function!");
        }
        log.info("Refresh token with key = {} has expired! Refresh token is: {}", expireRefreshToken.getId(),
                expireRefreshToken.getToken());
    }
}

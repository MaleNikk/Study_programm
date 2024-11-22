package com.fluxing.security.event;

import com.fluxing.security.dto.RefreshTokenJwt;
import com.fluxing.security.exception.RefreshTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRedisEvent {

    private final Logger log = LoggerFactory.getLogger(ApplicationRedisEvent.class);

    @EventListener
    public void handlerRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshTokenJwt> event) {

        log.info("Init check time token expiration in redis configuration!");

        RefreshTokenJwt expiredToken = (RefreshTokenJwt) event.getValue();

        if (expiredToken == null) {
            throw new RefreshTokenException("Refresh token is null in handle redis key expired function!");
        }

        log.info("Refresh token with key = {} has expired! Refresh token is: {}",
                expiredToken.getId(), expiredToken.getToken());
    }

}

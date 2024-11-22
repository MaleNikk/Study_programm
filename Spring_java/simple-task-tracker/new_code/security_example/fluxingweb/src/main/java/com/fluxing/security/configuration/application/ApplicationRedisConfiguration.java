package com.fluxing.security.configuration.application;

import com.fluxing.security.dto.RefreshTokenJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(keyspaceConfiguration = ApplicationRedisConfiguration.RefreshTokenKeySpaseConfiguration.class,
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class ApplicationRedisConfiguration {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final Logger log = LoggerFactory.getLogger(ApplicationRedisConfiguration.class);

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {

        log.info("Init redis configuration!");

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    public class RefreshTokenKeySpaseConfiguration extends KeyspaceConfiguration {

        private static final String REFRESH_TOKEN_KEYSPACE = "refresh_tokens";

        @Override
        @NonNull
        protected Iterable<KeyspaceSettings> initialConfiguration() {

            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(RefreshTokenJwt.class, REFRESH_TOKEN_KEYSPACE);

            keyspaceSettings.setTimeToLive(refreshTokenExpiration.toMillis());

            return Collections.singleton(keyspaceSettings);
        }
    }

}

package spring.boot.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableConfigurationProperties(AppCacheProperties.class)
@Configuration
public class AppCacheConfiguration {


/*
    @Bean
    @ConditionalOnExpression("'${com.cache.cacheType}'.equals('inMemory')")
   public ConcurrentMapCacheManager inMemoryCacheManager(AppCacheProperties cacheProperties) {

        var cacheManager = (ConcurrentMapCacheManager) createConcurrentMapCache(name) -> {

        return new ConcurrentMapCacheManager(name,
       CacheBuilder.newBuilder()
       .expiryAfterWrite(appCacheProperties.getCaches().get(name).getExpiry())
       .build()
       .asMap(),
        true);//

        };

        var cacheNames = appCacheProperties.getCacheNames();

        if (!cacheNames.isEmpty()) {
        cacheManager.setCacheNames(cacheNames);
    }

    return cacheManager;

    */


    @ConditionalOnProperty(prefix = "app.redis", name = "enable", havingValue = "true")
    @Bean
    public RedisCacheManager redisManager(AppCacheProperties cacheProperties, LettuceConnectionFactory lettuceFactory) {

        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();

        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();

        cacheProperties.getCacheNames().forEach(CN -> {
            cacheConfigurationMap.put(CN,
                    RedisCacheConfiguration
                            .defaultCacheConfig()
                            .entryTtl(cacheProperties
                                    .getCaches()
                                    .get(CN)
                                    .getExpiry()));
        });

        return RedisCacheManager.builder(lettuceFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
    }
}

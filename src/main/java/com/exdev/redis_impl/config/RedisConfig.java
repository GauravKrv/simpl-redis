package com.exdev.redis_impl.config;

import com.exdev.redis_impl.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {
    /*
        Pass these credentials for example using Jenkins secret manager, or simply use properties file (not secure for production environment)
     */
    @Value("${REDIS_HOST:localhost}")
    private String redisHost;

    @Value("${REDIS_PORT:6379}")
    private int redisPort;

    @Value("${REDIS_PASSWORD:}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);
        log.info("Printing redis host: {}",redisHost);
        log.info("Printing redis redisPort: {}",redisPort);
        return new LettuceConnectionFactory(config);
    }

    /**
     * Creates and configures a Redis-based CacheManager.
     *
     * This CacheManager uses Redis as the underlying caching provider and supports
     * multiple cache configurations with different TTL (time-to-live) values.
     *
     * @param redisConnectionFactory the factory to create Redis connections
     * @return a CacheManager instance configured with custom cache settings
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        // Default configuration (for caches that don't have specific TTL)
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))  // Default TTL of 1 hour
                .disableCachingNullValues();

        // Custom configuration for timeline cache with TTL of 30 minutes
        RedisCacheConfiguration thirtyMinutesCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // 30 minutes TTL
                .disableCachingNullValues();
        // Custom configuration for timeline cache with TTL of 30 minutes
        RedisCacheConfiguration testCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // 5 minutes TTL
                .disableCachingNullValues();

        // Custom configuration for user details cache with TTL of 1 hour
        RedisCacheConfiguration oneHourCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))  // 1 hour TTL
                .disableCachingNullValues();

        // Default configuration (for caches that don't have specific TTL)
        RedisCacheConfiguration oneDayCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))  // Default TTL of 1 hour
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> thirtyMinuteCacheConfigurationMap = CacheConstants.THIRTY_MINUTES_CACHE_CONFIGS.stream()
                        .collect(Collectors.toMap(
                                Function.identity(), // Key mapper: Use the element as the key
                                config -> thirtyMinutesCacheConfig // Value mapper: Use the same RedisCacheConfiguration for all keys
                        ));
        Map<String, RedisCacheConfiguration> oneHourCacheConfigurationMap = CacheConstants.ONE_HOUR_CACHE_CONFIGS.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        config -> oneHourCacheConfig
                ));
        Map<String, RedisCacheConfiguration> oneDayCacheConfigurationMap = CacheConstants.ONE_DAY_CACHE_CONFIGS.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        config -> oneDayCacheConfig
                ));

        // Return a cache manager with multiple cache configurations
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(thirtyMinuteCacheConfigurationMap)
                .withInitialCacheConfigurations(oneHourCacheConfigurationMap)
                .withInitialCacheConfigurations(oneDayCacheConfigurationMap)
                .build();
    }

    /**
     * Configures a RedisTemplate bean for interacting with Redis.
     *
     * This RedisTemplate is configured with specific serializers for both keys and values,
     * ensuring that data is stored and retrieved in a predictable format.
     *
     * @param redisConnectionFactory the factory to create Redis connections
     * @return a configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}






package com.redis.test.redis_spring_test_harness.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;

import redis.clients.jedis.JedisPoolConfig;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    /**
     * Jedis Connection Factory with connection pooling
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);

        if (password != null && !password.isEmpty()) {
            redisConfig.setPassword(password);
        }

        // Jedis Pool Configuration
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWait(Duration.ofMillis(-1));
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);

        JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                //.connectTimeout(Duration.ofSeconds(2))
                //.readTimeout(Duration.ofSeconds(2))
                .build();

        return new JedisConnectionFactory(redisConfig, jedisClientConfig);
    }

    /**
     * RedisTemplate with String keys and Object values (JSON serialization)
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use String serialization for keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Use JSON serialization for values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * StringRedisTemplate for simple String operations
     * More convenient for basic key-value operations
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}

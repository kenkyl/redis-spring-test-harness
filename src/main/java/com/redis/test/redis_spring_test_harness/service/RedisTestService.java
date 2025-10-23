package com.redis.test.redis_spring_test_harness.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTestService {

    private static final Logger log = LoggerFactory.getLogger(RedisTestService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisTestService(RedisTemplate<String, Object> redisTemplate,
                            StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Initialization method - called on application startup
     * Use this to set up test data, clear keys, or run setup commands
     */
    @PostConstruct
    public void initialize() {
        log.info("=== Initializing Redis Test Service ===");

        // Test connection
        try {
            String pong = stringRedisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();
            log.info("Redis connection test: {}", pong);
        } catch (Exception e) {
            log.error("Failed to connect to Redis", e);
            return;
        }

        // Optional: Clear test keys on startup
        // cleanupTestKeys();

        // Optional: Inject initial test data
        // seedTestData();

        log.info("=== Redis Test Service Initialized ===");
    }

    /**
     * Main execution method for your test code
     * Add your Redis commands and testing logic here
     */
    public void executeTests() {
        log.info("=== Starting Redis Test Execution ===");

        // ========================================
        // ADD YOUR TEST CODE BELOW
        // ========================================

        // Example: Simple String operations
        stringRedisTemplate.opsForValue().set("test:key", "Hello Redis!");
        String value = stringRedisTemplate.opsForValue().get("test:key");
        log.info("Retrieved value: {}", value);

        // Example: String with expiration
        stringRedisTemplate.opsForValue().set("test:expiring", "Will expire", 10, TimeUnit.SECONDS);

        // Example: Counter
        stringRedisTemplate.opsForValue().increment("test:counter");
        Long counter = stringRedisTemplate.opsForValue().increment("test:counter");
        log.info("Counter value: {}", counter);

        // Example: Hash operations
        // stringRedisTemplate.opsForHash().put("test:hash", "field1", "value1");
        // String hashValue = (String) stringRedisTemplate.opsForHash().get("test:hash", "field1");

        // Example: List operations
        // stringRedisTemplate.opsForList().rightPush("test:list", "item1");
        // stringRedisTemplate.opsForList().rightPush("test:list", "item2");

        // Example: Set operations
        // stringRedisTemplate.opsForSet().add("test:set", "member1", "member2", "member3");

        // Example: Sorted Set operations
        // stringRedisTemplate.opsForZSet().add("test:zset", "member1", 1.0);
        // stringRedisTemplate.opsForZSet().add("test:zset", "member2", 2.0);

        // ========================================
        // YOUR TEST CODE ENDS HERE
        // ========================================

        log.info("=== Redis Test Execution Completed ===");
    }

    /**
     * Optional: Seed initial test data
     */
    private void seedTestData() {
        log.info("Seeding test data...");

        // Add your seed data here
        stringRedisTemplate.opsForValue().set("seed:key1", "Seed Value 1");
        stringRedisTemplate.opsForValue().set("seed:key2", "Seed Value 2");

        log.info("Test data seeded");
    }

    /**
     * Optional: Clean up test keys
     */
    private void cleanupTestKeys() {
        log.info("Cleaning up test keys...");

        // Delete keys matching pattern
        Set<String> keys = stringRedisTemplate.keys("test:*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
            log.info("Deleted {} test keys", keys.size());
        }
    }

    /**
     * Get RedisTemplate for advanced operations
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * Get StringRedisTemplate for simple String operations
     */
    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }
}

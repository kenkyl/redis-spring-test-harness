package com.redis.test.redis_spring_test_harness;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class RedisSpringTestHarnessApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(RedisSpringTestHarnessApplication.class, args);
//	}
//
//}

import com.redis.test.redis_spring_test_harness.service.RedisTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisSpringTestHarnessApplication {

	private static final Logger log = LoggerFactory.getLogger(RedisSpringTestHarnessApplication.class);

	public static void main(String[] args) {
		log.info("Starting Redis Test Harness Application...");
		SpringApplication.run(RedisSpringTestHarnessApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RedisTestService redisTestService) {
		return args -> {
			log.info("=== Redis Test Harness Started ===");

			// Execute your test code
			redisTestService.executeTests();

			// Optional: Keep application running for monitoring
			// Thread.sleep(60000); // Keep alive for 1 minute

			log.info("=== Redis Test Harness Completed ===");

			// Application will exit after this unless you add wait logic
		};
	}
}
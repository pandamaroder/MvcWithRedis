package com.example.demo;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.4.0"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {

        REDIS_CONTAINER.start();

        TestPropertyValues.of(
                "spring.redis.host=" + REDIS_CONTAINER.getRedisHost(),
                "spring.redis.port=" + REDIS_CONTAINER.getRedisPort()
        ).applyTo(context.getEnvironment());

    }
}

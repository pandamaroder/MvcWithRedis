package com.example.demo;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final RedisContainer CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.4.0"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        CONTAINER.start();

        TestPropertyValues.of(
                "spring.data.redis.host=" + CONTAINER.getRedisHost(),
                "spring.data.redis.port=" + CONTAINER.getRedisPort()
        ).applyTo(context.getEnvironment());
    }
}

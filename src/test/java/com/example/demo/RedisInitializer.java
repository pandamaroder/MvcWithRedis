package com.example.demo;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final GenericContainer<?> CONTAINER = new GenericContainer<>(DockerImageName.parse("redis").withTag("4.0.14"))
            .withExposedPorts(6379);

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        CONTAINER.start();

        TestPropertyValues.of(
                "spring.data.redis.host=" + CONTAINER.getHost(),
                "spring.data.redis.port=" + CONTAINER.getMappedPort(6379)
        ).applyTo(context.getEnvironment());
    }
}

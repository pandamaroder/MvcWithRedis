package com.example.demo;

import com.redis.testcontainers.RedisContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final RedisContainer REDIS_CONTAINER =
        new RedisContainer(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379);

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        REDIS_CONTAINER.start();

        final String redisUrl = String.format("redis://%s:%d",
            REDIS_CONTAINER.getHost(),
            REDIS_CONTAINER.getFirstMappedPort());

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            context,
            "spring.redis.url=" + redisUrl
        );
    }
}

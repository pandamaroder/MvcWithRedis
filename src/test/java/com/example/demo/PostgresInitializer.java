package com.example.demo;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
                    .withUrlParam("prepareThreshold", "0")
                    //.withUsername()
                    .waitingFor(Wait.forListeningPort());

    @Override
    public void initialize(final ConfigurableApplicationContext context) {
        CONTAINER
            .start();

        TestPropertyValues.of(
            "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
            "spring.datasource.username=" + CONTAINER.getUsername(),
            "spring.datasource.password=" + CONTAINER.getPassword()
        ).applyTo(context.getEnvironment());
    }
}

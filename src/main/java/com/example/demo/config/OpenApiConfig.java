package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfiguration.
 * Конфигурация swagger для генерации документации API.
 *
 * @author Olga_Kukushkina
 */
@Configuration
public class OpenApiConfig {

    /*    @Value("${swagger.info.title}")
    private String title;

    @Value("${swagger.info.description}")
    private String description;*/

    /*    @Value("${swagger.security.schemeName}")
    private String securitySchemeName;

    @Value("${swagger.security.format}")
    private String securityFormat;*/

    /*   */

    /**
     * Создание OpenAPI элемента, описывающего пространства api.
     *
     * @return {@link OpenAPI} описание документации для пространства api.
     *//*
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
           // .addSecurityItem(getSecurityItem())
            .components(getComponents())
            .info(apiInfo());
    }*/
    @Bean
    public GroupedOpenApi categoriesApi() {
        return GroupedOpenApi.builder()
            .group("categories")
            .pathsToMatch("/categories/**")
            .build();
    }

    @Bean
    public GroupedOpenApi newsApi() {

        return GroupedOpenApi
            .builder()
            .group("news")
            .pathsToMatch("/news/**")
            .build();
    }

    @Bean
    public GroupedOpenApi commentsApi() {
        return GroupedOpenApi.builder()
            .group("comments")
            .pathsToMatch("/comments/**")
            .build();
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
            .group("users")
            .pathsToMatch("/users/**")
            .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("News Service API")
                .version("1.0.0")
                .description("API documentation for News Service"));
    }
}

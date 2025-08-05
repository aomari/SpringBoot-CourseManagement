package com.coursemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3 configuration for Swagger UI documentation.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:CourseManagement}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .description("RESTful API for managing an educational system with instructors, students, courses, and reviews. " +
                                    "Built with Spring Boot, JPA, and PostgreSQL using UUID primary keys.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Course Management Team")
                                .email("support@coursemanagement.com")
                                .url("https://github.com/aomari/SpringBoot-CourseManagement"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.coursemanagement.com")
                                .description("Production server")));
    }
}
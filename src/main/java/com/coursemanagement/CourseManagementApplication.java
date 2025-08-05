package com.coursemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Spring Boot application class for Course Management System.
 * 
 * This application provides a RESTful API for managing an educational system
 * with instructors, students, courses, and reviews.
 * 
 * Features:
 * - RESTful API with Spring Web
 * - JPA with PostgreSQL database
 * - UUID primary keys
 * - Data validation
 * - Swagger/OpenAPI documentation
 * - Global exception handling
 * - Layered architecture (Controller, Service, Repository)
 */
@SpringBootApplication(scanBasePackages = "com.coursemanagement")
@EnableTransactionManagement
public class CourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApplication.class, args);
    }
}
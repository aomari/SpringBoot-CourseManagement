# ⚙️ Configuration Reference

Complete configuration reference for the Course Management System.

## Overview

This document covers all configuration options available in the Course Management System, including database settings, server configuration, logging, and third-party integrations.

## 📁 Configuration Files

### application.properties (Main Configuration)

Located at `src/main/resources/application.properties`

```properties
# ============================================================================
# DATABASE CONFIGURATION
# ============================================================================

# PostgreSQL Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/course_management_db
spring.datasource.username=admin
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# ============================================================================
# JPA/HIBERNATE CONFIGURATION
# ============================================================================

# Database Platform
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# DDL Generation Strategy
# Options: none, validate, update, create, create-drop
spring.jpa.hibernate.ddl-auto=validate

# SQL Logging
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Hibernate Properties
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true

# ============================================================================
# CONNECTION POOL (HIKARICP) CONFIGURATION
# ============================================================================

# Pool Size Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Connection Timeout Configuration
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000

# Performance and Monitoring
spring.datasource.hikari.leak-detection-threshold=60000
spring.datasource.hikari.pool-name=CourseManagementHikariCP

# ============================================================================
# SERVER CONFIGURATION
# ============================================================================

# Server Port
server.port=8080

# Context Path
server.servlet.context-path=/

# Session Configuration
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false

# Compression
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
server.compression.min-response-size=1024

# ============================================================================
# SWAGGER/OPENAPI CONFIGURATION
# ============================================================================

# API Documentation Paths
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Swagger UI Configuration
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.try-it-out-enabled=true
springdoc.swagger-ui.filter=true

# OpenAPI Configuration
springdoc.api-docs.groups.enabled=true
springdoc.group-configs[0].group=public
springdoc.group-configs[0].paths-to-match=/api/v1/**

# ============================================================================
# ACTUATOR CONFIGURATION
# ============================================================================

# Actuator Endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.endpoint.health.show-components=always

# Metrics Configuration
management.metrics.export.prometheus.enabled=true
management.metrics.distribution.percentiles-histogram.http.server.requests=true

# ============================================================================
# LOGGING CONFIGURATION
# ============================================================================

# Root Logging Level
logging.level.root=INFO

# Application Logging
logging.level.com.coursemanagement=INFO

# Framework Logging
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN

# File Logging
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# Log Pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# ============================================================================
# VALIDATION CONFIGURATION
# ============================================================================

# Bean Validation
spring.jpa.properties.javax.persistence.validation.mode=auto

# ============================================================================
# FLYWAY CONFIGURATION (DATABASE MIGRATIONS)
# ============================================================================

# Flyway Settings
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true

# ============================================================================
# JACKSON CONFIGURATION (JSON SERIALIZATION)
# ============================================================================

# JSON Properties
spring.jackson.default-property-inclusion=NON_NULL
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.serialization.indent-output=false
spring.jackson.deserialization.fail-on-unknown-properties=false

# Date Format
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss
spring.jackson.time-zone=UTC
```

### application-dev.properties (Development)

Located at `src/main/resources/application-dev.properties`

```properties
# Development-specific configuration

# Database
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.com.coursemanagement=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Actuator (expose all endpoints in dev)
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

# Swagger UI (enable try-it-out in dev)
springdoc.swagger-ui.try-it-out-enabled=true

# HikariCP (smaller pool for development)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2

# Server
server.error.include-stacktrace=always
server.error.include-message=always
```

### application-test.properties (Testing)

Located at `src/test/resources/application-test.properties`

```properties
# Test-specific configuration

# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration for Tests
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Disable Flyway for tests
spring.flyway.enabled=false

# Logging
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Disable Actuator security for tests
management.security.enabled=false
```

### application-prod.properties (Production)

Located at `src/main/resources/application-prod.properties`

```properties
# Production-specific configuration

# Database (use environment variables)
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/course_management_db}
spring.datasource.username=${DATABASE_USERNAME:admin}
spring.datasource.password=${DATABASE_PASSWORD:admin123}

# JPA Configuration
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate

# Logging (less verbose in production)
logging.level.com.coursemanagement=WARN
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=ERROR

# HikariCP (larger pool for production)
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10

# Server Configuration
server.compression.enabled=true
server.servlet.session.cookie.secure=true

# Actuator (limited exposure in production)
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=never

# Swagger UI (disable in production)
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

## 🌍 Environment Variables

### Database Configuration

```bash
# Database Connection
export DATABASE_URL="jdbc:postgresql://localhost:5432/course_management_db"
export DATABASE_USERNAME="admin"
export DATABASE_PASSWORD="admin123"

# Connection Pool
export HIKARI_MAXIMUM_POOL_SIZE="20"
export HIKARI_MINIMUM_IDLE="5"
export HIKARI_CONNECTION_TIMEOUT="30000"
```

### Server Configuration

```bash
# Server Settings
export SERVER_PORT="8080"
export SERVER_CONTEXT_PATH="/"

# SSL Configuration (if using HTTPS)
export SERVER_SSL_ENABLED="true"
export SERVER_SSL_KEY_STORE="classpath:keystore.p12"
export SERVER_SSL_KEY_STORE_PASSWORD="password"
export SERVER_SSL_KEY_STORE_TYPE="PKCS12"
```

### Logging Configuration

```bash
# Log Levels
export LOGGING_LEVEL_ROOT="INFO"
export LOGGING_LEVEL_COURSEMANAGEMENT="INFO"
export LOGGING_LEVEL_SPRING="WARN"

# Log File
export LOGGING_FILE_NAME="logs/application.log"
export LOGGING_FILE_MAX_SIZE="10MB"
export LOGGING_FILE_MAX_HISTORY="30"
```

## 🗄️ Database Configuration Details

### Connection Pool Settings

```properties
# Pool Size Configuration
spring.datasource.hikari.maximum-pool-size=20        # Maximum connections
spring.datasource.hikari.minimum-idle=5              # Minimum idle connections
spring.datasource.hikari.connection-timeout=30000    # Connection timeout (30s)
spring.datasource.hikari.idle-timeout=300000         # Idle timeout (5m)
spring.datasource.hikari.max-lifetime=600000         # Maximum connection lifetime (10m)
spring.datasource.hikari.leak-detection-threshold=60000  # Connection leak detection (60s)
```

### JPA/Hibernate Settings

```properties
# DDL Generation Strategies
spring.jpa.hibernate.ddl-auto=none        # No DDL generation
spring.jpa.hibernate.ddl-auto=validate    # Validate schema (recommended for production)
spring.jpa.hibernate.ddl-auto=update      # Update schema (use with caution)
spring.jpa.hibernate.ddl-auto=create      # Create schema (development only)
spring.jpa.hibernate.ddl-auto=create-drop # Create and drop (testing only)

# Performance Tuning
spring.jpa.properties.hibernate.jdbc.batch_size=20           # Batch size for inserts/updates
spring.jpa.properties.hibernate.order_inserts=true          # Order inserts for batching
spring.jpa.properties.hibernate.order_updates=true          # Order updates for batching
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true  # Batch versioned data
```

## 🔧 Server Configuration Details

### Tomcat Configuration

```properties
# Embedded Tomcat Settings
server.tomcat.max-threads=200                    # Maximum worker threads
server.tomcat.min-spare-threads=10              # Minimum spare threads
server.tomcat.max-connections=8192               # Maximum connections
server.tomcat.accept-count=100                   # Accept queue size
server.tomcat.connection-timeout=20000           # Connection timeout (20s)

# Request Size Limits
server.tomcat.max-http-post-size=2MB            # Maximum POST size
server.tomcat.max-swallow-size=2MB              # Maximum request body size
```

### Session Configuration

```properties
# Session Settings
server.servlet.session.timeout=30m              # Session timeout
server.servlet.session.cookie.name=JSESSIONID   # Session cookie name
server.servlet.session.cookie.http-only=true    # HTTP-only cookies
server.servlet.session.cookie.secure=false      # Secure cookies (HTTPS only)
server.servlet.session.cookie.same-site=lax     # SameSite attribute
```

## 📊 Monitoring Configuration

### Actuator Endpoints

```properties
# Endpoint Configuration
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoints.web.base-path=/actuator

# Health Endpoint
management.endpoint.health.show-details=when-authorized
management.endpoint.health.show-components=always
management.endpoint.health.probes.enabled=true

# Info Endpoint
management.endpoint.info.enabled=true
management.info.build.enabled=true
management.info.git.enabled=true
management.info.env.enabled=true

# Metrics Endpoint
management.endpoint.metrics.enabled=true
management.metrics.export.prometheus.enabled=true
```

### Custom Health Indicators

```properties
# Database Health Check
management.health.db.enabled=true

# Disk Space Health Check
management.health.diskspace.enabled=true
management.health.diskspace.path=/
management.health.diskspace.threshold=10MB
```

## 📝 Logging Configuration Details

### Log Levels

```properties
# Application Logging Levels
logging.level.com.coursemanagement=INFO
logging.level.com.coursemanagement.service=DEBUG
logging.level.com.coursemanagement.controller=INFO
logging.level.com.coursemanagement.repository=WARN

# Framework Logging Levels
logging.level.org.springframework=INFO
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=INFO
logging.level.org.hibernate=WARN
logging.level.org.hibernate.SQL=WARN
logging.level.com.zaxxer.hikari=INFO
```

### Log File Configuration

```properties
# File Logging
logging.file.name=logs/application.log
logging.file.path=logs/
logging.file.max-size=10MB
logging.file.max-history=30
logging.file.total-size-cap=1GB

# Rolling Policy
logging.logback.rollingpolicy.file-name-pattern=logs/application-%d{yyyy-MM-dd}.%i.gz
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=30
```

### Log Patterns

```properties
# Console Logging Pattern
logging.pattern.console=%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx

# File Logging Pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# Custom Pattern with Request ID
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

## 🔒 Security Configuration

### CORS Configuration

```properties
# CORS Settings
cors.allowed-origins=http://localhost:3000,http://localhost:8080
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true
cors.max-age=3600
```

### SSL/TLS Configuration

```properties
# SSL Configuration
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=coursemanagement

# SSL Protocol
server.ssl.protocol=TLS
server.ssl.enabled-protocols=TLSv1.2,TLSv1.3
server.ssl.ciphers=TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
```

## 🧪 Testing Configuration

### Test Profiles

```properties
# Test Database Configuration
spring.test.database.replace=none
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Test-specific Settings
spring.jpa.show-sql=true
logging.level.org.springframework.test=DEBUG
management.endpoints.web.exposure.include=*
```

### Test Containers Configuration

```yaml
# testcontainers.properties
testcontainers.reuse.enable=true
testcontainers.ryuk.disabled=false
```

## ⚡ Performance Tuning

### JVM Options

```bash
# Memory Settings
-Xms512m -Xmx2g

# Garbage Collection
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+UseStringDeduplication

# JIT Compilation
-XX:+TieredCompilation
-XX:TieredStopAtLevel=1

# Monitoring
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=logs/
```

### Database Optimization

```properties
# Connection Pool Tuning
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Hibernate Performance
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.jdbc.fetch_size=50
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
```

## 🔧 Configuration Best Practices

### 1. Environment-Specific Configuration

```bash
# Use Spring profiles
java -jar app.jar --spring.profiles.active=prod

# Use environment variables for sensitive data
export DATABASE_PASSWORD="secure_password"
```

### 2. Externalized Configuration

```bash
# Configuration file location
java -jar app.jar --spring.config.location=file:./config/

# Additional configuration
java -jar app.jar --spring.config.additional-location=file:./custom/
```

### 3. Configuration Validation

```java
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    
    @NotBlank
    @Email
    private String adminEmail;
    
    @Min(1)
    @Max(100)
    private int maxUsers;
    
    // getters and setters
}
```

## 📚 Related Documentation

- **[Getting Started Guide](../guides/getting-started.md)** - Basic configuration setup
- **[Architecture Guide](../guides/architecture.md)** - System design and configuration patterns
- **[Database Schema](./database-schema.md)** - Database configuration details
- **[Troubleshooting Guide](./troubleshooting.md)** - Configuration-related issues

---

*Proper configuration is key to a robust, scalable, and maintainable application. Use environment-specific profiles and externalize sensitive configuration.* ⚙️
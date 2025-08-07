# 🚀 Getting Started Guide

Complete setup and configuration guide for the Course Management System.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

### Required Software
- **Java 17+** - [Download OpenJDK](https://adoptopenjdk.net/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi) (or use included wrapper)
- **Docker & Docker Compose** - [Download Docker](https://www.docker.com/get-started)
- **Git** - [Download Git](https://git-scm.com/downloads)

### Optional Tools
- **PostgreSQL Client** (psql) - For database management
- **Postman** - For API testing
- **IntelliJ IDEA** or **VS Code** - For development

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/course-management.git
cd course-management
```

### 2. Verify Java Installation

```bash
java -version
# Should show Java 17 or higher
```

### 3. Verify Docker Installation

```bash
docker --version
docker-compose --version
```

## 🗄️ Database Setup

### Option 1: Using Docker Compose (Recommended)

The project includes a complete Docker Compose configuration for PostgreSQL with pgAdmin.

```bash
# Start the database
docker-compose up -d

# Verify containers are running
docker-compose ps

# Check logs
docker-compose logs postgres
```

**Database Configuration:**
- **Host**: localhost
- **Port**: 5432
- **Database**: course_management_db
- **Username**: admin
- **Password**: admin123

**pgAdmin Access** (Optional):
- **URL**: http://localhost:5050
- **Email**: admin@coursemanagement.com
- **Password**: admin123

### Option 2: Using Docker Run Command

```bash
docker run -d \
  --name course-db \
  -e POSTGRES_DB=course_management_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  -v course-db-data:/var/lib/postgresql/data \
  postgres:16
```

### Option 3: Local PostgreSQL Installation

If you prefer to use a local PostgreSQL installation:

1. Install PostgreSQL 16+
2. Create database and user:

```sql
CREATE DATABASE course_management_db;
CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE course_management_db TO admin;
```

3. Update `application.properties` with your local configuration

## ⚙️ Application Configuration

### Default Configuration

The application comes with sensible defaults in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/course_management_db
spring.datasource.username=admin
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000

# Server Configuration
server.port=8080
server.servlet.context-path=/

# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# Logging Configuration
logging.level.com.coursemanagement=INFO
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=WARN
```

### Environment-Specific Configuration

Create environment-specific property files:

**Development** (`application-dev.properties`):
```properties
spring.jpa.show-sql=true
logging.level.com.coursemanagement=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

**Production** (`application-prod.properties`):
```properties
spring.jpa.show-sql=false
logging.level.com.coursemanagement=WARN
spring.datasource.hikari.maximum-pool-size=50
```

## 🏗️ Building the Application

### Using Maven Wrapper (Recommended)

```bash
# Make wrapper executable (Linux/Mac)
chmod +x mvnw

# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Package application
./mvnw clean package

# Skip tests during packaging
./mvnw clean package -DskipTests
```

### Using System Maven

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn clean package
```

## 🚀 Running the Application

### Method 1: Using Maven (Development)

```bash
# Run with default profile
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run with custom port
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Method 2: Using JAR File (Production)

```bash
# Build the JAR
./mvnw clean package

# Run the JAR
java -jar target/course-management-0.0.1-SNAPSHOT.jar

# Run with specific profile
java -jar target/course-management-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Run with custom configuration
java -jar target/course-management-0.0.1-SNAPSHOT.jar --server.port=8081 --spring.datasource.url=jdbc:postgresql://localhost:5432/prod_db
```

### Method 3: Using Docker

```bash
# Build Docker image
docker build -t course-management:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/course_management_db \
  course-management:latest
```

## ✅ Verification

### 1. Health Check

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 2. API Documentation

Visit these URLs in your browser:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### 3. Database Connection

```bash
# Using Docker container
docker exec -it course-db psql -U admin -d course_management_db

# Using local psql client
psql -h localhost -p 5432 -U admin -d course_management_db

# List tables
\dt

# Check if tables exist
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';
```

## 🧪 First API Calls

### Create Your First Instructor

```bash
curl -X POST http://localhost:8080/api/v1/instructors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "instructorDetails": {
      "youtubeChannel": "https://youtube.com/@johndoe",
      "hoppy": "Teaching Java and Spring Boot"
    }
  }'
```

### Create a Course

```bash
# First, get the instructor ID from the previous response, then:
curl -X POST http://localhost:8080/api/v1/courses \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Fundamentals",
    "instructorId": "YOUR_INSTRUCTOR_ID_HERE"
  }'
```

### Create a Student

```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.johnson@example.com"
  }'
```

### Enroll Student in Course

```bash
curl -X POST http://localhost:8080/api/v1/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "YOUR_STUDENT_ID_HERE",
    "courseId": "YOUR_COURSE_ID_HERE"
  }'
```

### Get All Data

```bash
# Get all instructors
curl http://localhost:8080/api/v1/instructors

# Get all courses
curl http://localhost:8080/api/v1/courses

# Get all students
curl http://localhost:8080/api/v1/students

# Get all enrollments
curl http://localhost:8080/api/v1/enrollments
```

## 🛠️ Development Setup

### IDE Configuration

#### IntelliJ IDEA

1. **Import Project**:
   - File → Open → Select project directory
   - Choose "Import as Maven project"

2. **Configure JDK**:
   - File → Project Structure → Project → Project SDK → Select Java 17+

3. **Enable Annotation Processing**:
   - Settings → Build → Compiler → Annotation Processors → Enable

4. **Install Plugins** (Optional):
   - Lombok Plugin
   - Spring Boot Plugin
   - Database Navigator

#### VS Code

1. **Install Extensions**:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client

2. **Configure Java**:
   - Ctrl+Shift+P → "Java: Configure Runtime" → Set Java 17+

### Database Management Tools

#### Using pgAdmin (Docker)

1. Access pgAdmin at http://localhost:5050
2. Login with admin@coursemanagement.com / admin123
3. Add server:
   - Host: postgres (or localhost if not using Docker)
   - Port: 5432
   - Username: admin
   - Password: admin123

#### Using DBeaver (Recommended)

1. Download [DBeaver Community Edition](https://dbeaver.io/download/)
2. Create new connection:
   - Database: PostgreSQL
   - Host: localhost
   - Port: 5432
   - Database: course_management_db
   - Username: admin
   - Password: admin123

## 🧪 Running Tests

### Full Test Suite

```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Specific Test Categories

```bash
# Unit tests only
./mvnw test -Dtest="**/*ServiceTest"

# Integration tests only
./mvnw test -Dtest="**/*RepositoryTest"

# Controller tests only
./mvnw test -Dtest="**/*ControllerTest"
```

## 🔍 Monitoring and Debugging

### Application Logs

```bash
# View application logs
tail -f logs/application.log

# View Docker container logs
docker-compose logs -f course-management

# View database logs
docker-compose logs -f postgres
```

### Spring Boot Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Metrics
curl http://localhost:8080/actuator/metrics

# Environment properties
curl http://localhost:8080/actuator/env
```

### Database Monitoring

```bash
# Check active connections
docker exec -it course-db psql -U admin -d course_management_db -c "SELECT count(*) FROM pg_stat_activity;"

# Check database size
docker exec -it course-db psql -U admin -d course_management_db -c "SELECT pg_size_pretty(pg_database_size('course_management_db'));"
```

## 🚨 Troubleshooting

### Common Issues

#### 1. Port Already in Use

```bash
# Check what's using port 8080
lsof -i :8080

# Kill process using port 8080
kill -9 $(lsof -ti:8080)

# Or run on different port
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### 2. Database Connection Issues

```bash
# Check if PostgreSQL container is running
docker ps | grep postgres

# Check container logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres

# Reset database
docker-compose down -v
docker-compose up -d
```

#### 3. Maven Build Issues

```bash
# Clean Maven cache
./mvnw dependency:purge-local-repository

# Refresh dependencies
./mvnw clean compile -U

# Skip tests if they're causing issues
./mvnw clean package -DskipTests
```

#### 4. Java Version Issues

```bash
# Check Java version
java -version

# Set JAVA_HOME (Linux/Mac)
export JAVA_HOME=/path/to/java17

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\path\to\java17
```

### Getting Help

1. **Check the logs** - Most issues are logged with helpful error messages
2. **Verify prerequisites** - Ensure all required software is properly installed
3. **Check the [Troubleshooting Guide](../reference/troubleshooting.md)** - Common issues and solutions
4. **Review the [Configuration Reference](../reference/configuration.md)** - All configuration options

## 🎯 Next Steps

Now that you have the application running:

1. **Explore the APIs** - Use the [API Documentation](../api/) to understand all available endpoints
2. **Run Tests** - Follow the [Testing Guide](./testing.md) to run and understand the test suite
3. **Understand the Architecture** - Read the [Architecture Guide](./architecture.md) to understand the system design
4. **Customize Configuration** - Check the [Configuration Reference](../reference/configuration.md) for advanced options

## 📚 Related Documentation

- **[Architecture Guide](./architecture.md)** - System design and patterns
- **[Testing Guide](./testing.md)** - Comprehensive testing strategy
- **[API Documentation](../api/)** - Complete API reference
- **[Configuration Reference](../reference/configuration.md)** - All configuration options
- **[Database Schema](../reference/database-schema.md)** - Entity relationships and database design

---

**Congratulations! 🎉 You now have the Course Management System running locally. Happy coding!**
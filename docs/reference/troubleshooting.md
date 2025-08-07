# 🚨 Troubleshooting Guide

Comprehensive troubleshooting guide for common issues in the Course Management System.

## 🎯 Quick Diagnosis

### System Health Check

```bash
# 1. Check application health
curl http://localhost:8080/actuator/health

# 2. Check database connectivity
docker exec -it course-db psql -U admin -d course_management_db -c "SELECT 1;"

# 3. Check application logs
tail -f logs/application.log

# 4. Check Docker containers
docker-compose ps
```

### Common Issue Indicators

| Symptom | Likely Cause | Quick Fix |
|---------|--------------|-----------|
| 404 on all endpoints | Application not started | Check if app is running on correct port |
| 500 errors | Database connection issue | Verify database is running |
| Slow responses | Database connection pool exhausted | Check HikariCP metrics |
| Memory errors | Insufficient heap space | Increase JVM memory |
| Connection refused | Port already in use | Check port usage with `lsof -i :8080` |

## 🗄️ Database Issues

### Database Connection Problems

#### Issue: Cannot connect to PostgreSQL database

**Symptoms:**
```
org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

**Solutions:**

1. **Check if PostgreSQL is running:**
```bash
# Check Docker container status
docker-compose ps

# If not running, start it
docker-compose up -d postgres

# Check container logs
docker-compose logs postgres
```

2. **Verify database configuration:**
```properties
# Check application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/course_management_db
spring.datasource.username=admin
spring.datasource.password=admin123
```

3. **Test database connectivity:**
```bash
# Test connection directly
psql -h localhost -p 5432 -U admin -d course_management_db

# Or using Docker
docker exec -it course-db psql -U admin -d course_management_db
```

#### Issue: Database authentication failed

**Symptoms:**
```
org.postgresql.util.PSQLException: FATAL: password authentication failed for user "admin"
```

**Solutions:**

1. **Reset database credentials:**
```bash
# Stop containers
docker-compose down

# Remove volumes (WARNING: This will delete all data)
docker-compose down -v

# Start fresh
docker-compose up -d
```

2. **Check environment variables:**
```bash
# Verify Docker environment
docker exec -it course-db env | grep POSTGRES
```

#### Issue: Connection pool exhausted

**Symptoms:**
```
HikariPool-1 - Connection is not available, request timed out after 30000ms
```

**Solutions:**

1. **Increase pool size:**
```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
```

2. **Check for connection leaks:**
```properties
spring.datasource.hikari.leak-detection-threshold=60000
```

3. **Monitor active connections:**
```sql
SELECT count(*) as active_connections 
FROM pg_stat_activity 
WHERE state = 'active';
```

### Schema and Migration Issues

#### Issue: Table doesn't exist

**Symptoms:**
```
org.postgresql.util.PSQLException: ERROR: relation "instructors" does not exist
```

**Solutions:**

1. **Check DDL auto setting:**
```properties
# For development (creates tables automatically)
spring.jpa.hibernate.ddl-auto=create-drop

# For production (validates existing schema)
spring.jpa.hibernate.ddl-auto=validate
```

2. **Manual table creation:**
```sql
-- Connect to database and create tables manually
-- See database-schema.md for complete schema
```

3. **Run Flyway migrations:**
```bash
./mvnw flyway:migrate
```

#### Issue: Schema validation failed

**Symptoms:**
```
SchemaManagementException: Schema validation failed
```

**Solutions:**

1. **Update schema to match entities:**
```bash
# Set to update mode temporarily
spring.jpa.hibernate.ddl-auto=update
```

2. **Reset database:**
```bash
docker-compose down -v
docker-compose up -d
```

## 🚀 Application Startup Issues

### Port Already in Use

**Symptoms:**
```
Port 8080 was already in use
```

**Solutions:**

1. **Find and kill process using port:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or kill all processes on port 8080
lsof -ti:8080 | xargs kill -9
```

2. **Run on different port:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

3. **Change default port:**
```properties
server.port=8081
```

### Java Version Issues

**Symptoms:**
```
UnsupportedClassVersionError: com/coursemanagement/CourseManagementApplication has been compiled by a more recent version of the Java Runtime
```

**Solutions:**

1. **Check Java version:**
```bash
java -version
javac -version
```

2. **Set correct JAVA_HOME:**
```bash
# Linux/Mac
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH

# Windows
set JAVA_HOME=C:\path\to\java17
set PATH=%JAVA_HOME%\bin;%PATH%
```

3. **Use Maven wrapper with specific Java:**
```bash
JAVA_HOME=/path/to/java17 ./mvnw spring-boot:run
```

### Memory Issues

**Symptoms:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solutions:**

1. **Increase heap size:**
```bash
# For Maven
export MAVEN_OPTS="-Xmx2g"
./mvnw spring-boot:run

# For JAR execution
java -Xmx2g -jar target/course-management-*.jar
```

2. **Monitor memory usage:**
```bash
# Check JVM memory
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Check system memory
free -h
```

## 🌐 API and Web Issues

### 404 Not Found on API Endpoints

**Symptoms:**
```
404 Not Found for /api/v1/instructors
```

**Solutions:**

1. **Check application context path:**
```properties
server.servlet.context-path=/
```

2. **Verify controller mapping:**
```java
@RequestMapping("/api/v1/instructors")
@RestController
public class InstructorController {
    // Controller methods
}
```

3. **Check if controller is being scanned:**
```java
@SpringBootApplication
@ComponentScan(basePackages = "com.coursemanagement")
public class CourseManagementApplication {
    // Main method
}
```

### JSON Serialization Issues

**Symptoms:**
```
HttpMessageNotReadableException: JSON parse error
```

**Solutions:**

1. **Check JSON format:**
```bash
# Validate JSON using online tools or:
echo '{"firstName":"John"}' | python -m json.tool
```

2. **Configure Jackson properly:**
```properties
spring.jackson.deserialization.fail-on-unknown-properties=false
spring.jackson.serialization.write-dates-as-timestamps=false
```

3. **Check DTO annotations:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorRequest {
    // Fields with proper annotations
}
```

### CORS Issues

**Symptoms:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solutions:**

1. **Configure CORS globally:**
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

2. **Use @CrossOrigin annotation:**
```java
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class InstructorController {
    // Controller methods
}
```

## 🧪 Testing Issues

### Test Database Issues

**Symptoms:**
```
Tests failing with database connection errors
```

**Solutions:**

1. **Check test configuration:**
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

2. **Use @TestPropertySource:**
```java
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class InstructorServiceTest {
    // Test methods
}
```

3. **Use TestContainers for integration tests:**
```java
@SpringBootTest
@Testcontainers
class IntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

### Mock Issues

**Symptoms:**
```
Wanted but not invoked: Actually, there were zero interactions with this mock
```

**Solutions:**

1. **Check mock setup:**
```java
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    @Mock
    private Repository repository;
    
    @InjectMocks
    private ServiceImpl service;
    
    @Test
    void test() {
        // Arrange
        when(repository.findById(any())).thenReturn(Optional.of(entity));
        
        // Act
        service.method();
        
        // Assert
        verify(repository).findById(any());
    }
}
```

2. **Use @MockBean for Spring context:**
```java
@SpringBootTest
class IntegrationTest {
    @MockBean
    private ExternalService externalService;
    
    @Test
    void test() {
        // Test with mocked external service
    }
}
```

### Coverage Issues

**Symptoms:**
```
JaCoCo coverage below threshold
```

**Solutions:**

1. **Check JaCoCo configuration:**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <rules>
            <rule>
                <element>CLASS</element>
                <limits>
                    <limit>
                        <counter>INSTRUCTION</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</plugin>
```

2. **Exclude generated code:**
```xml
<configuration>
    <excludes>
        <exclude>**/dto/**</exclude>
        <exclude>**/config/**</exclude>
        <exclude>**/*Application.class</exclude>
    </excludes>
</configuration>
```

## 🐳 Docker Issues

### Container Startup Problems

**Symptoms:**
```
Container exits immediately or fails to start
```

**Solutions:**

1. **Check Docker logs:**
```bash
docker-compose logs postgres
docker-compose logs course-management
```

2. **Verify Docker Compose file:**
```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: course_management_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

3. **Check port conflicts:**
```bash
# Check if port is already in use
lsof -i :5432

# Use different port if needed
ports:
  - "5433:5432"
```

### Volume Issues

**Symptoms:**
```
Database data not persisting between restarts
```

**Solutions:**

1. **Check volume configuration:**
```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
```

2. **List Docker volumes:**
```bash
docker volume ls
docker volume inspect coursemanagement_postgres_data
```

3. **Reset volumes if corrupted:**
```bash
docker-compose down -v
docker-compose up -d
```

## 🔧 Performance Issues

### Slow Database Queries

**Symptoms:**
- API responses taking several seconds
- High CPU usage on database

**Solutions:**

1. **Enable SQL logging:**
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

2. **Add database indexes:**
```sql
-- Add indexes for commonly queried columns
CREATE INDEX idx_instructors_email ON instructors(email);
CREATE INDEX idx_courses_instructor_id ON courses(instructor_id);
```

3. **Optimize queries:**
```java
// Use JOIN FETCH to avoid N+1 queries
@Query("SELECT i FROM Instructor i LEFT JOIN FETCH i.instructorDetails WHERE i.id = :id")
Optional<Instructor> findByIdWithDetails(@Param("id") UUID id);
```

### Memory Leaks

**Symptoms:**
- Increasing memory usage over time
- OutOfMemoryError after running for a while

**Solutions:**

1. **Monitor memory usage:**
```bash
# Check JVM memory metrics
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Generate heap dump
jcmd <PID> GC.run_finalization
jcmd <PID> VM.gc
jmap -dump:format=b,file=heapdump.hprof <PID>
```

2. **Check for connection leaks:**
```properties
spring.datasource.hikari.leak-detection-threshold=60000
```

3. **Profile with tools:**
```bash
# Use VisualVM, JProfiler, or similar tools
# Add JVM flags for profiling
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=logs/
```

## 📊 Monitoring and Debugging

### Enable Debug Logging

```properties
# Application debug
logging.level.com.coursemanagement=DEBUG

# Spring debug
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG

# Database debug
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# HikariCP debug
logging.level.com.zaxxer.hikari=DEBUG
```

### Useful Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Metrics
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# HTTP trace (if enabled)
curl http://localhost:8080/actuator/httptrace

# Thread dump
curl http://localhost:8080/actuator/threaddump

# Environment properties
curl http://localhost:8080/actuator/env
```

### Database Debugging Queries

```sql
-- Check active connections
SELECT 
    pid,
    usename,
    application_name,
    client_addr,
    state,
    query_start,
    query
FROM pg_stat_activity 
WHERE state = 'active';

-- Check table sizes
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Check index usage
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

## 🆘 Getting Help

### Log Analysis

1. **Check application logs:**
```bash
tail -f logs/application.log
grep -i error logs/application.log
grep -i exception logs/application.log
```

2. **Check system logs:**
```bash
# Linux
journalctl -u docker
dmesg | grep -i error

# Docker logs
docker-compose logs --tail=100
```

### Collecting Diagnostic Information

Before seeking help, collect this information:

1. **System Information:**
```bash
java -version
docker --version
docker-compose --version
uname -a
```

2. **Application Configuration:**
```bash
# Sanitized configuration (remove passwords)
cat src/main/resources/application.properties | grep -v password
```

3. **Error Logs:**
```bash
# Last 100 lines of application log
tail -100 logs/application.log

# Any stack traces
grep -A 20 -B 5 "Exception" logs/application.log
```

4. **System Resources:**
```bash
free -h
df -h
ps aux | grep java
```

### Community Resources

- **Stack Overflow**: Tag questions with `spring-boot`, `postgresql`, `docker`
- **Spring Boot Documentation**: https://docs.spring.io/spring-boot/docs/current/reference/html/
- **PostgreSQL Documentation**: https://www.postgresql.org/docs/
- **Docker Documentation**: https://docs.docker.com/

---

## 📚 Related Documentation

- **[Getting Started Guide](../guides/getting-started.md)** - Initial setup and configuration
- **[Configuration Reference](./configuration.md)** - All configuration options
- **[Database Schema](./database-schema.md)** - Database structure and relationships
- **[Architecture Guide](../guides/architecture.md)** - System design and components

---

*Remember: Most issues can be resolved by carefully reading error messages and checking logs. When in doubt, start with the basics: is the database running? Is the application started? Are the ports available?* 🚨
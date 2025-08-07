# 🧪 Testing Guide

Comprehensive testing strategy and implementation guide for the Course Management System.

## Overview

This guide covers the complete testing setup for the Course Management System, including unit tests, integration tests, web layer tests, and code coverage analysis with JaCoCo.

## 🎯 Testing Strategy

### Testing Pyramid Approach

```
    /\
   /  \     E2E Tests (Few)
  /____\
 /      \   Integration Tests (Some)
/________\  Unit Tests (Many)
```

### Coverage Goals
- **Instruction Coverage**: ≥ 80%
- **Branch Coverage**: ≥ 70%
- **Line Coverage**: ≥ 85%

## 🏗️ Test Categories

### 1. Unit Tests
- **Purpose**: Test individual components in isolation
- **Scope**: Service layer business logic
- **Tools**: JUnit 5, Mockito, AssertJ
- **Location**: `src/test/java/.../service/`

### 2. Integration Tests
- **Purpose**: Test database interactions and repository layer
- **Scope**: Repository methods, JPA queries, database operations
- **Tools**: `@DataJpaTest`, TestEntityManager, H2 in-memory database
- **Location**: `src/test/java/.../repository/`

### 3. Web Layer Tests
- **Purpose**: Test REST API endpoints and request/response handling
- **Scope**: Controller layer, serialization, validation, HTTP status codes
- **Tools**: `@WebMvcTest`, MockMvc, JSON assertions
- **Location**: `src/test/java/.../controller/`

## 🛠️ Test Configuration

### Test Dependencies (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Test Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- H2 Database for Testing -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Testcontainers (Optional for PostgreSQL integration tests) -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### JaCoCo Configuration
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
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
                            <limit>
                                <counter>BRANCH</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.70</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Test Application Properties
```properties
# src/test/resources/application-test.properties

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
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
```

## 📝 Writing Tests

### Unit Test Example (Service Layer)

```java
@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private InstructorDetailsRepository instructorDetailsRepository;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    @Test
    @DisplayName("Should create instructor successfully")
    void shouldCreateInstructorSuccessfully() {
        // Given
        InstructorRequest request = InstructorRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

        Instructor savedInstructor = Instructor.builder()
            .id(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

        when(instructorRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(savedInstructor);

        // When
        InstructorResponse response = instructorService.createInstructor(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        
        verify(instructorRepository).existsByEmail(request.getEmail());
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    @DisplayName("Should throw exception when instructor already exists")
    void shouldThrowExceptionWhenInstructorAlreadyExists() {
        // Given
        InstructorRequest request = InstructorRequest.builder()
            .email("john.doe@example.com")
            .build();

        when(instructorRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> instructorService.createInstructor(request))
            .isInstanceOf(ResourceAlreadyExistsException.class)
            .hasMessageContaining("Instructor already exists with email");
    }
}
```

### Integration Test Example (Repository Layer)

```java
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class InstructorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("Should find instructor by email")
    void shouldFindInstructorByEmail() {
        // Given
        Instructor instructor = Instructor.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
        
        entityManager.persistAndFlush(instructor);

        // When
        Optional<Instructor> found = instructorRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should return empty when instructor not found by email")
    void shouldReturnEmptyWhenInstructorNotFoundByEmail() {
        // When
        Optional<Instructor> found = instructorRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find instructors by name containing ignore case")
    void shouldFindInstructorsByNameContainingIgnoreCase() {
        // Given
        Instructor instructor1 = Instructor.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
            
        Instructor instructor2 = Instructor.builder()
            .firstName("Jane")
            .lastName("Smith")
            .email("jane.smith@example.com")
            .build();

        entityManager.persistAndFlush(instructor1);
        entityManager.persistAndFlush(instructor2);

        // When
        List<Instructor> found = instructorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("john", "john");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFirstName()).isEqualTo("John");
    }
}
```

### Web Layer Test Example (Controller Layer)

```java
@WebMvcTest(InstructorController.class)
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create instructor successfully")
    void shouldCreateInstructorSuccessfully() throws Exception {
        // Given
        InstructorRequest request = InstructorRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

        InstructorResponse response = InstructorResponse.builder()
            .id(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .fullName("John Doe")
            .email("john.doe@example.com")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(instructorService.createInstructor(any(InstructorRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(instructorService).createInstructor(any(InstructorRequest.class));
    }

    @Test
    @DisplayName("Should return validation error for invalid request")
    void shouldReturnValidationErrorForInvalidRequest() throws Exception {
        // Given
        InstructorRequest invalidRequest = InstructorRequest.builder()
            .firstName("")
            .lastName("")
            .email("invalid-email")
            .build();

        // When & Then
        mockMvc.perform(post("/api/v1/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    @DisplayName("Should return 404 when instructor not found")
    void shouldReturn404WhenInstructorNotFound() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        when(instructorService.getInstructorById(instructorId))
            .thenThrow(new ResourceNotFoundException("Instructor not found with id: " + instructorId));

        // When & Then
        mockMvc.perform(get("/api/v1/instructors/{id}", instructorId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Instructor not found with id: " + instructorId));
    }
}
```

## 🚀 Running Tests

### Command Line Options

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report

# Run specific test class
./mvnw test -Dtest=InstructorServiceTest

# Run specific test method
./mvnw test -Dtest=InstructorServiceTest#shouldCreateInstructorSuccessfully

# Run tests in specific package
./mvnw test -Dtest="com.coursemanagement.service.*"

# Skip tests during build
./mvnw clean package -DskipTests

# Run tests with detailed output
./mvnw test -Dtest.verbose=true
```

### IDE Integration

**IntelliJ IDEA:**
- Right-click on test class/method → "Run Test"
- Use Ctrl+Shift+F10 to run tests
- View coverage: Run → "Run with Coverage"

**VS Code:**
- Install "Java Test Runner" extension
- Click the play button next to test methods
- Use Test Explorer panel

## 📊 Code Coverage

### Generating Coverage Reports

```bash
# Generate JaCoCo report
./mvnw test jacoco:report

# View HTML report
open target/site/jacoco/index.html

# Generate XML report for CI/CD
./mvnw test jacoco:report -Djacoco.outputDirectory=target/coverage-reports
```

### Coverage Report Structure

```
target/site/jacoco/
├── index.html              # Main coverage report
├── com.coursemanagement/
│   ├── service/
│   │   └── impl/
│   │       └── InstructorServiceImpl.html
│   ├── controller/
│   │   └── InstructorController.html
│   └── repository/
│       └── InstructorRepository.html
└── jacoco.xml              # XML format for CI/CD tools
```

### Understanding Coverage Metrics

- **Instruction Coverage**: Percentage of bytecode instructions executed
- **Branch Coverage**: Percentage of if/else branches executed
- **Line Coverage**: Percentage of source code lines executed
- **Method Coverage**: Percentage of methods executed
- **Class Coverage**: Percentage of classes with at least one method executed

## 🎯 Testing Best Practices

### 1. Test Naming Convention
```java
// Pattern: should[ExpectedBehavior]When[StateUnderTest]
@Test
@DisplayName("Should create instructor successfully when valid request is provided")
void shouldCreateInstructorSuccessfullyWhenValidRequestIsProvided() {
    // Test implementation
}
```

### 2. AAA Pattern (Arrange, Act, Assert)
```java
@Test
void shouldCalculateAverageRating() {
    // Arrange
    List<Review> reviews = Arrays.asList(
        createReview(5), createReview(4), createReview(3)
    );
    
    // Act
    double average = reviewService.calculateAverageRating(reviews);
    
    // Assert
    assertThat(average).isEqualTo(4.0);
}
```

### 3. Use AssertJ for Fluent Assertions
```java
// Instead of JUnit assertions
assertThat(instructors)
    .hasSize(3)
    .extracting(Instructor::getEmail)
    .containsExactly("john@example.com", "jane@example.com", "bob@example.com");
```

### 4. Test Data Builders
```java
public class InstructorTestDataBuilder {
    public static Instructor createInstructor() {
        return Instructor.builder()
            .id(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
    
    public static InstructorRequest createInstructorRequest() {
        return InstructorRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
    }
}
```

### 5. Mock External Dependencies
```java
@MockBean
private EmailService emailService; // External service

@Test
void shouldSendEmailWhenInstructorCreated() {
    // Given
    InstructorRequest request = createInstructorRequest();
    
    // When
    instructorService.createInstructor(request);
    
    // Then
    verify(emailService).sendWelcomeEmail(eq(request.getEmail()));
}
```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Tests Failing Due to Database State
```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InstructorRepositoryTest {
    // Test methods
}
```

#### 2. MockMvc Not Finding Controller Endpoints
```java
@WebMvcTest(InstructorController.class)
@Import({InstructorController.class}) // Explicit import
class InstructorControllerTest {
    // Test methods
}
```

#### 3. H2 Database Schema Issues
```sql
-- src/test/resources/schema.sql
CREATE TABLE IF NOT EXISTS instructors (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

#### 4. JaCoCo Not Generating Reports
```xml
<!-- Ensure proper plugin configuration -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <configuration>
        <destFile>${project.build.directory}/coverage-reports/jacoco-unit.exec</destFile>
        <dataFile>${project.build.directory}/coverage-reports/jacoco-unit.exec</dataFile>
    </configuration>
</plugin>
```

## 📈 Current Test Status

### ✅ Implemented Tests
- **InstructorServiceTest**: 25 unit tests - All passing
- **InstructorRepositoryTest**: 25 integration tests - Mostly working
- **Basic application context loading test**: Working

### ⚠️ Partially Working
- **Repository tests**: Minor assertion issues in search functionality

### ❌ Need Fixing
- **InstructorControllerTest**: All 23 tests failing due to endpoint mapping issues
- **Controller tests**: Need proper `@WebMvcTest` configuration

### 🎯 Next Steps
1. Fix controller test endpoint mapping issues
2. Add tests for Course, Student, Review, and Enrollment entities
3. Implement integration tests with TestContainers
4. Add performance tests for high-load scenarios
5. Set up continuous integration with automated testing

---

## 📚 Related Documentation

- **[Getting Started Guide](./getting-started.md)** - Setup and configuration
- **[Architecture Guide](./architecture.md)** - System design and patterns
- **[API Documentation](../api/)** - Complete API reference
- **[Configuration Reference](../reference/configuration.md)** - Configuration options

---

*Happy Testing! 🧪 Remember: Good tests are the foundation of maintainable software.*
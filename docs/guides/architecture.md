# 🏗️ System Architecture Guide

Comprehensive guide to the Course Management System's architecture, design patterns, and technical decisions.

## 🎯 Overview

The Course Management System follows a **layered architecture** pattern with clean separation of concerns, implementing modern Spring Boot best practices for scalability, maintainability, and testability.

## 📐 Architectural Patterns

### 1. Layered Architecture (N-Tier)

```
┌─────────────────┐
│  Presentation   │ ← Controllers, DTOs, REST APIs
│     Layer       │
├─────────────────┤
│    Business     │ ← Services, Business Logic
│     Layer       │
├─────────────────┤
│ Data Access     │ ← Repositories, JPA Entities
│     Layer       │
├─────────────────┤
│   Database      │ ← PostgreSQL, Tables, Indexes
│     Layer       │
└─────────────────┘
```

**Benefits:**
- Clear separation of concerns
- Easy to test and maintain
- Scalable and modular design
- Framework-agnostic business logic

### 2. Dependency Injection Pattern

```java
@Service
@RequiredArgsConstructor  // Constructor injection via Lombok
public class InstructorServiceImpl implements InstructorService {
    
    private final InstructorRepository instructorRepository;
    private final InstructorDetailsRepository instructorDetailsRepository;
    
    // Business logic methods
}
```

**Benefits:**
- Loose coupling between components
- Easy mocking for testing
- Framework manages object lifecycle
- Promotes single responsibility principle

### 3. Repository Pattern

```java
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    
    Optional<Instructor> findByEmail(String email);
    
    @Query("SELECT i FROM Instructor i WHERE " +
           "LOWER(i.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Instructor> findByNameContaining(@Param("name") String name);
}
```

**Benefits:**
- Abstracts data access logic
- Centralized query management
- Easy to mock for testing
- Database-agnostic business layer

## 🏢 System Components

### 1. Presentation Layer

#### Controllers
```java
@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
@Validated
public class InstructorController {
    
    private final InstructorService instructorService;
    
    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructor(
            @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.createInstructor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

**Responsibilities:**
- Handle HTTP requests/responses
- Input validation
- Data transformation (Entity ↔ DTO)
- Exception handling
- API documentation

#### DTOs (Data Transfer Objects)
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorRequest {
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;
    
    @Valid
    private InstructorDetailsRequest instructorDetails;
}
```

**Benefits:**
- API contract definition
- Input validation
- Decoupling from internal entities
- Version compatibility

### 2. Business Layer

#### Service Interfaces
```java
public interface InstructorService {
    InstructorResponse createInstructor(InstructorRequest request);
    InstructorResponse getInstructorById(UUID id);
    List<InstructorResponse> getAllInstructors();
    InstructorResponse updateInstructor(UUID id, InstructorRequest request);
    void deleteInstructor(UUID id);
    // Additional business methods
}
```

#### Service Implementations
```java
@Service
@RequiredArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService {
    
    private final InstructorRepository instructorRepository;
    private final InstructorDetailsRepository instructorDetailsRepository;
    
    @Override
    public InstructorResponse createInstructor(InstructorRequest request) {
        // Business logic validation
        if (instructorRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Instructor already exists with email: " + request.getEmail());
        }
        
        // Entity creation and persistence
        Instructor instructor = InstructorMapper.toEntity(request);
        Instructor savedInstructor = instructorRepository.save(instructor);
        
        return InstructorMapper.toResponse(savedInstructor);
    }
}
```

**Responsibilities:**
- Business logic implementation
- Transaction management
- Data validation
- Cross-cutting concerns (logging, security)

### 3. Data Access Layer

#### JPA Entities
```java
@Entity
@Table(name = "instructors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_details_id")
    private InstructorDetails instructorDetails;
    
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

#### Repository Layer
```java
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    
    Optional<Instructor> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT i FROM Instructor i LEFT JOIN FETCH i.instructorDetails WHERE i.id = :id")
    Optional<Instructor> findByIdWithDetails(@Param("id") UUID id);
    
    @Query("SELECT i FROM Instructor i WHERE i.instructorDetails IS NOT NULL")
    List<Instructor> findAllWithDetails();
}
```

## 🔗 Entity Relationships

### Database Schema Overview

```
┌─────────────────┐    ┌─────────────────────┐
│   Instructors   │    │ InstructorDetails   │
│                 │    │                     │
│ • id (UUID)     │◄──►│ • id (UUID)         │
│ • first_name    │    │ • youtube_channel   │
│ • last_name     │ 1:1│ • hoppy             │
│ • email         │    │ • created_at        │
│ • created_at    │    │ • updated_at        │
│ • updated_at    │    │                     │
└─────────────────┘    └─────────────────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐
│     Courses     │
│                 │
│ • id (UUID)     │
│ • title         │
│ • instructor_id │
│ • created_at    │
│ • updated_at    │
└─────────────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐    ┌─────────────────┐
│     Reviews     │    │   Enrollments   │
│                 │    │                 │
│ • id (UUID)     │    │ • id (UUID)     │
│ • course_id     │    │ • student_id    │
│ • student_id    │    │ • course_id     │
│ • comment       │    │ • enrolled_at   │
│ • rating        │    │                 │
│ • created_at    │    └─────────────────┘
│ • updated_at    │             │
└─────────────────┘             │ N:1
         ▲                      ▼
         │ N:1           ┌─────────────────┐
         └───────────────┤    Students     │
                         │                 │
                         │ • id (UUID)     │
                         │ • first_name    │
                         │ • last_name     │
                         │ • email         │
                         │ • created_at    │
                         │ • updated_at    │
                         └─────────────────┘
```

### Relationship Types

1. **One-to-One**: Instructor ↔ InstructorDetails
   - Optional relationship
   - Cascade operations
   - Lazy loading

2. **One-to-Many**: Instructor → Courses
   - Bidirectional mapping
   - Cascade delete
   - Lazy loading

3. **Many-to-One**: Course ← Reviews
   - Required relationship
   - Foreign key constraint

4. **Many-to-Many**: Student ↔ Course (through Enrollments)
   - Join table pattern
   - Additional enrollment metadata

## 🛡️ Cross-Cutting Concerns

### 1. Exception Handling

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("NOT_FOUND")
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> ValidationError.builder()
                .field(error.getField())
                .rejectedValue(error.getRejectedValue())
                .message(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());
            
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("VALIDATION_FAILED")
            .message("Validation failed for one or more fields")
            .validationErrors(validationErrors)
            .timestamp(LocalDateTime.now())
            .build();
            
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

### 2. Transaction Management

```java
@Service
@Transactional  // Default: readOnly = false, propagation = REQUIRED
public class CourseServiceImpl implements CourseService {
    
    @Transactional(readOnly = true)  // Read-only optimization
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
            .stream()
            .map(CourseMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(rollbackFor = Exception.class)  // Explicit rollback
    public CourseResponse createCourse(CourseRequest request) {
        // Transactional operations
    }
}
```

### 3. Validation Strategy

```java
// Entity-level validation
@Entity
public class Course {
    @NotBlank(message = "Course title is required")
    @Size(max = 200, message = "Course title must be less than 200 characters")
    private String title;
}

// DTO-level validation
@Data
public class CourseRequest {
    @NotBlank(message = "Course title is required")
    @Size(max = 200, message = "Course title must be less than 200 characters")
    private String title;
    
    @NotNull(message = "Instructor ID is required")
    private UUID instructorId;
}

// Service-level validation
@Service
public class CourseServiceImpl {
    public CourseResponse createCourse(CourseRequest request) {
        // Business rule validation
        if (!instructorRepository.existsById(request.getInstructorId())) {
            throw new ResourceNotFoundException("Instructor not found");
        }
    }
}
```

## 🔧 Configuration Architecture

### 1. Database Configuration

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.coursemanagement.repository")
public class DatabaseConfig {
    
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource() {
        return new HikariDataSource();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
```

### 2. API Documentation Configuration

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Course Management API",
        version = "1.0",
        description = "RESTful API for managing courses, instructors, and students"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Development server"),
        @Server(url = "https://api.coursemanagement.com", description = "Production server")
    }
)
public class OpenApiConfig {
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/v1/**")
            .build();
    }
}
```

## 📊 Performance Considerations

### 1. Database Optimization

```java
// Optimized queries with JOIN FETCH
@Query("SELECT c FROM Course c " +
       "LEFT JOIN FETCH c.instructor " +
       "LEFT JOIN FETCH c.reviews r " +
       "LEFT JOIN FETCH r.student " +
       "WHERE c.id = :id")
Optional<Course> findByIdWithDetails(@Param("id") UUID id);

// Pagination for large datasets
@Query("SELECT c FROM Course c ORDER BY c.createdAt DESC")
Page<Course> findAllOrderByCreatedAtDesc(Pageable pageable);
```

### 2. Connection Pool Configuration

```properties
# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.leak-detection-threshold=60000
```

### 3. Caching Strategy (Future Enhancement)

```java
@Service
@CacheConfig(cacheNames = "instructors")
public class InstructorServiceImpl {
    
    @Cacheable(key = "#id")
    public InstructorResponse getInstructorById(UUID id) {
        // Method implementation
    }
    
    @CacheEvict(key = "#id")
    public InstructorResponse updateInstructor(UUID id, InstructorRequest request) {
        // Method implementation
    }
}
```

## 🔒 Security Architecture (Future Enhancement)

### 1. Authentication & Authorization

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .build();
    }
}
```

### 2. Method-Level Security

```java
@Service
public class InstructorServiceImpl {
    
    @PreAuthorize("hasRole('ADMIN') or #request.email == authentication.name")
    public InstructorResponse updateInstructor(UUID id, InstructorRequest request) {
        // Method implementation
    }
    
    @PostAuthorize("hasRole('ADMIN') or returnObject.email == authentication.name")
    public InstructorResponse getInstructorById(UUID id) {
        // Method implementation
    }
}
```

## 🚀 Scalability Patterns

### 1. Microservice Readiness

The current monolithic structure is designed to be easily split into microservices:

```
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│ Instructor      │  │ Course          │  │ Student         │
│ Service         │  │ Service         │  │ Service         │
│                 │  │                 │  │                 │
│ • Instructors   │  │ • Courses       │  │ • Students      │
│ • Details       │  │ • Reviews       │  │ • Enrollments   │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

### 2. Event-Driven Architecture

```java
@Component
public class CourseEventPublisher {
    
    @EventListener
    public void handleStudentEnrolled(StudentEnrolledEvent event) {
        // Send notification
        // Update statistics
        // Trigger other business processes
    }
}
```

### 3. CQRS Pattern (Command Query Responsibility Segregation)

```java
// Command side - Write operations
@Service
public class InstructorCommandService {
    public InstructorResponse createInstructor(CreateInstructorCommand command) {
        // Handle write operations
    }
}

// Query side - Read operations
@Service
public class InstructorQueryService {
    public List<InstructorResponse> findInstructors(InstructorQuery query) {
        // Handle read operations with optimized queries
    }
}
```

## 🧪 Testing Architecture

### 1. Test Pyramid Implementation

```java
// Unit Tests (Fast, Isolated)
@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {
    @Mock private InstructorRepository repository;
    @InjectMocks private InstructorServiceImpl service;
}

// Integration Tests (Medium, Database)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class InstructorRepositoryTest {
    @Autowired private TestEntityManager entityManager;
    @Autowired private InstructorRepository repository;
}

// End-to-End Tests (Slow, Full Stack)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class InstructorControllerIntegrationTest {
    @Container static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    @Autowired private TestRestTemplate restTemplate;
}
```

## 📈 Monitoring and Observability

### 1. Actuator Endpoints

```properties
# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.metrics.export.prometheus.enabled=true
```

### 2. Custom Metrics

```java
@Component
public class CourseMetrics {
    
    private final Counter courseCreatedCounter;
    private final Timer courseCreationTimer;
    
    public CourseMetrics(MeterRegistry meterRegistry) {
        this.courseCreatedCounter = Counter.builder("courses.created")
            .description("Total number of courses created")
            .register(meterRegistry);
            
        this.courseCreationTimer = Timer.builder("courses.creation.time")
            .description("Time taken to create a course")
            .register(meterRegistry);
    }
}
```

## 🎯 Design Principles Applied

### 1. SOLID Principles

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes must be substitutable for their base types
- **Interface Segregation**: Many specific interfaces rather than one general-purpose interface
- **Dependency Inversion**: Depend on abstractions, not concretions

### 2. Clean Architecture

- **Independence**: Framework, database, and UI independent
- **Testability**: Business rules can be tested without external elements
- **Independence of UI**: UI can change without changing the rest of the system
- **Independence of Database**: Business rules are not bound to the database

### 3. Domain-Driven Design (DDD) Elements

- **Entities**: Core business objects with identity
- **Value Objects**: Immutable objects without identity
- **Repositories**: Abstraction for data access
- **Services**: Domain logic that doesn't belong to entities
- **Aggregates**: Consistency boundaries

## 📚 Related Documentation

- **[Getting Started Guide](./getting-started.md)** - Setup and configuration
- **[Testing Guide](./testing.md)** - Comprehensive testing strategy
- **[API Documentation](../api/)** - Complete API reference
- **[Database Schema](../reference/database-schema.md)** - Entity relationships and database design
- **[Configuration Reference](../reference/configuration.md)** - All configuration options

---

*This architecture provides a solid foundation for building scalable, maintainable, and testable applications using modern Spring Boot practices.* 🏗️
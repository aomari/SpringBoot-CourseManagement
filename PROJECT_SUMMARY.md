# Course Management API - Project Summary

## 🚀 Overview
A comprehensive Spring Boot RESTful API for managing instructors and instructor details with a one-to-one relationship. Built following Spring Boot best practices with a clean layered architecture.

## 📁 Project Structure

```
src/main/java/com/coursemanagement/
├── CourseManagementApplication.java        # Main application class
├── config/
│   └── OpenApiConfig.java                  # Swagger/OpenAPI configuration
├── controller/
│   ├── InstructorController.java          # REST endpoints for instructors
│   └── InstructorDetailsController.java   # REST endpoints for instructor details
├── dto/
│   ├── InstructorRequest.java             # Request DTO for instructor operations
│   ├── InstructorResponse.java            # Response DTO for instructor operations
│   ├── InstructorDetailsRequest.java      # Request DTO for instructor details
│   └── InstructorDetailsResponse.java     # Response DTO for instructor details
├── entity/
│   ├── Instructor.java                    # JPA entity for instructors
│   └── InstructorDetails.java             # JPA entity for instructor details
├── exception/
│   ├── GlobalExceptionHandler.java        # Global exception handling
│   ├── ResourceNotFoundException.java     # Custom exception for not found resources
│   ├── ResourceAlreadyExistsException.java # Custom exception for conflicts
│   └── ErrorResponse.java                 # Standard error response format
├── repository/
│   ├── InstructorRepository.java          # Spring Data JPA repository for instructors
│   └── InstructorDetailsRepository.java   # Spring Data JPA repository for instructor details
└── service/
    ├── InstructorService.java             # Service interface for instructors
    ├── InstructorDetailsService.java      # Service interface for instructor details
    └── impl/
        ├── InstructorServiceImpl.java     # Service implementation for instructors
        └── InstructorDetailsServiceImpl.java # Service implementation for instructor details
```

## ✨ Key Features

### 🏗️ Architecture
- **Layered Architecture**: Controller → Service → Repository
- **Clean Separation**: DTOs for API, Entities for persistence
- **Dependency Injection**: Constructor-based injection
- **Transaction Management**: `@Transactional` annotations

### 🗃️ Database
- **PostgreSQL** with UUID primary keys
- **One-to-One relationship** between Instructor and InstructorDetails
- **JPA/Hibernate** for ORM
- **Audit fields**: createdAt, updatedAt with `@CreationTimestamp` and `@UpdateTimestamp`

### 🛡️ Validation & Error Handling
- **Bean Validation**: `@NotBlank`, `@Email`, `@Size`, `@Valid`
- **Global Exception Handler**: Centralized error handling with `@ControllerAdvice`
- **Custom Exceptions**: ResourceNotFoundException, ResourceAlreadyExistsException
- **Structured Error Response**: Consistent error format with validation details

### 📡 REST API
- **RESTful endpoints** with proper HTTP status codes
- **CRUD operations** for both entities
- **Search functionality**: By name, email, YouTube channel, hobby
- **Relationship management**: Add/remove instructor details

### 📚 API Documentation
- **Swagger/OpenAPI 3**: Interactive documentation at `/swagger-ui.html`
- **Comprehensive annotations**: `@Operation`, `@ApiResponse`, `@Schema`
- **Examples**: Request/response examples in all DTOs

## 🛠️ Technologies Used

- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **PostgreSQL Driver**
- **SpringDoc OpenAPI UI 2.2.0**
- **Java 17**
- **Maven**

## 📋 API Endpoints

### Instructor Endpoints
- `POST /api/v1/instructors` - Create instructor (with optional details)
- `GET /api/v1/instructors` - Get all instructors
- `GET /api/v1/instructors/{id}` - Get instructor by ID
- `PUT /api/v1/instructors/{id}` - Update instructor
- `DELETE /api/v1/instructors/{id}` - Delete instructor
- `GET /api/v1/instructors/email/{email}` - Get instructor by email
- `GET /api/v1/instructors/search?name={name}` - Search instructors by name
- `GET /api/v1/instructors/with-details` - Get instructors with details
- `GET /api/v1/instructors/without-details` - Get instructors without details
- `PUT /api/v1/instructors/{id}/details/{detailsId}` - Add details to instructor
- `DELETE /api/v1/instructors/{id}/details` - Remove details from instructor

### Instructor Details Endpoints
- `POST /api/v1/instructor-details` - Create instructor details
- `GET /api/v1/instructor-details` - Get all instructor details
- `GET /api/v1/instructor-details/{id}` - Get instructor details by ID
- `PUT /api/v1/instructor-details/{id}` - Update instructor details
- `DELETE /api/v1/instructor-details/{id}` - Delete instructor details
- `GET /api/v1/instructor-details/search/youtube?channel={channel}` - Search by YouTube channel
- `GET /api/v1/instructor-details/search/hobby?hobby={hobby}` - Search by hobby
- `GET /api/v1/instructor-details/orphaned` - Get orphaned details

## 🚀 Getting Started

### 1. Database Setup
```bash
# Start PostgreSQL using Docker
./start-db.sh

# Or manually with docker-compose
docker-compose up -d postgres
```

### 2. Run Application
```bash
# Using Maven Wrapper
./mvnw spring-boot:run

# Or compile and run
./mvnw clean compile
./mvnw spring-boot:run
```

### 3. Access Documentation
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### 4. Database Management (Optional)
- **pgAdmin**: http://localhost:5050 (admin@coursemanagement.com / admin123)

## 🧪 Example Requests

### Create Instructor with Details
```json
POST /api/v1/instructors
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "instructorDetails": {
    "youtubeChannel": "https://youtube.com/@johndoe",
    "hobby": "Teaching and coding"
  }
}
```

### Update Instructor
```json
PUT /api/v1/instructors/{id}
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com"
}
```

## 🔧 Configuration

### Database Configuration (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/course_management_db
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
```

### Swagger Configuration
```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🎯 Best Practices Implemented

1. **DTO Pattern**: Separate request/response models from entities
2. **Service Layer**: Business logic separated from controllers
3. **Constructor Injection**: Preferred over field injection
4. **Validation**: Input validation with custom error messages
5. **Exception Handling**: Global exception handling with detailed error responses
6. **Documentation**: Comprehensive API documentation with examples
7. **UUID Primary Keys**: Better security and distributed system support
8. **Transactional Management**: Proper transaction boundaries
9. **Repository Pattern**: Spring Data JPA repositories
10. **RESTful Design**: Proper HTTP methods and status codes

## 🚀 Future Enhancements

- [ ] Pagination and sorting for list endpoints
- [ ] Advanced search with filters
- [ ] Audit logging
- [ ] Security with JWT authentication
- [ ] Caching with Redis
- [ ] Metrics and monitoring
- [ ] Integration tests
- [ ] Docker containerization
- [ ] CI/CD pipeline

## 📈 Learning Outcomes

This project demonstrates:
- **Spring Boot ecosystem** usage
- **RESTful API design** principles
- **Database relationship** management
- **Error handling** strategies
- **API documentation** best practices
- **Validation** techniques
- **Layered architecture** implementation
- **Professional code structure** and organization

Perfect for **portfolio projects** and **learning Spring Boot fundamentals**!
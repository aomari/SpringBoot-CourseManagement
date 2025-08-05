# Course Management API

A comprehensive Spring Boot RESTful API for managing instructors and instructor details with PostgreSQL database. Features a clean layered architecture with full CRUD operations, validation, error handling, and Swagger documentation.

## 🚀 Features

- **RESTful API** for instructors and instructor details management
- **One-to-One relationship** between instructors and their details
- **Comprehensive validation** with custom error handling
- **Swagger/OpenAPI documentation** for interactive API testing
- **PostgreSQL database** with UUID primary keys
- **Clean architecture** with Controller → Service → Repository layers
- **Search functionality** by name, email, YouTube channel, and hobby

## 📁 Project Structure

```
src/main/java/com/coursemanagement/
├── CourseManagementApplication.java        # Main application class
├── config/
│   └── OpenApiConfig.java                  # Swagger/OpenAPI configuration
├── controller/
│   ├── InstructorController.java          # REST endpoints for instructors
│   └── InstructorDetailsController.java   # REST endpoints for instructor details
├── dto/                                    # Data Transfer Objects
├── entity/                                 # JPA entities
├── exception/                              # Global exception handling
├── repository/                             # Spring Data JPA repositories
└── service/                                # Business logic layer
```

## Prerequisites

- Docker and Docker Compose installed
- Java 17 or higher
- Maven (or use included Maven wrapper)

## Database Setup

### Option 1: Using Docker Compose (Recommended)

1. **Start the PostgreSQL container:**
   ```bash
   docker-compose up -d
   ```

2. **Check container status:**
   ```bash
   docker-compose ps
   ```

3. **View container logs:**
   ```bash
   docker-compose logs postgres
   ```

4. **Stop the container:**
   ```bash
   docker-compose down
   ```

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

## Database Connection

### Connect using psql (if installed):
```bash
psql -h localhost -p 5432 -U admin -d course_management_db
```

### Connect using Docker:
```bash
docker exec -it course-db psql -U admin -d course_management_db
```

### Database Credentials:
- **Host:** localhost
- **Port:** 5432
- **Database:** course_management_db
- **Username:** admin
- **Password:** admin123

## Application Setup

1. **Build the application:**
   ```bash
   ./mvnw clean compile
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application:**
   - **Main API**: http://localhost:8080
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **OpenAPI JSON**: http://localhost:8080/api-docs

## 📡 API Endpoints

### Instructor Management
- `POST /api/v1/instructors` - Create instructor (with optional details)
- `GET /api/v1/instructors` - Get all instructors
- `GET /api/v1/instructors/{id}` - Get instructor by ID
- `PUT /api/v1/instructors/{id}` - Update instructor
- `DELETE /api/v1/instructors/{id}` - Delete instructor
- `GET /api/v1/instructors/search?name={name}` - Search by name
- `GET /api/v1/instructors/email/{email}` - Get by email

### Instructor Details Management
- `POST /api/v1/instructor-details` - Create instructor details
- `GET /api/v1/instructor-details` - Get all instructor details
- `GET /api/v1/instructor-details/{id}` - Get details by ID
- `PUT /api/v1/instructor-details/{id}` - Update details
- `DELETE /api/v1/instructor-details/{id}` - Delete details
- `GET /api/v1/instructor-details/search/youtube?channel={channel}` - Search by YouTube
- `GET /api/v1/instructor-details/search/hobby?hobby={hobby}` - Search by hobby

## Useful Commands

### Container Management:
```bash
# Start container
docker-compose up -d

# Stop container
docker-compose down

# Restart container
docker-compose restart

# Remove container and volume
docker-compose down -v

# View logs
docker-compose logs -f postgres
```

### Maven Commands:
```bash
# Clean and compile
./mvnw clean compile

# Run application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Package application
./mvnw clean package

# Build without tests
./mvnw clean package -DskipTests
```

### Database Operations:
```bash
# Connect to database
docker exec -it course-db psql -U admin -d course_management_db

# List databases
docker exec -it course-db psql -U admin -c "\l"

# List tables (after connecting)
\dt

# Exit psql
\q
```

## 🧪 Example API Usage

### Create an Instructor with Details
```bash
curl -X POST "http://localhost:8080/api/v1/instructors" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "instructorDetails": {
      "youtubeChannel": "https://youtube.com/@johndoe",
      "hobby": "Teaching and coding"
    }
  }'
```

### Get All Instructors
```bash
curl -X GET "http://localhost:8080/api/v1/instructors"
```

### Search Instructors by Name
```bash
curl -X GET "http://localhost:8080/api/v1/instructors/search?name=John"
```

## 🛠️ Technologies Used

- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Web & Validation**
- **PostgreSQL**
- **SpringDoc OpenAPI UI**
- **Java 17**
- **Maven**

## Troubleshooting

### Port Already in Use:
If port 5432 is already in use, you can:
1. Stop existing PostgreSQL service: `sudo service postgresql stop`
2. Or change the port mapping in `docker-compose.yml`

### Container Won't Start:
1. Check if container name is already in use: `docker ps -a`
2. Remove existing container: `docker rm course-db`
3. Start fresh: `docker-compose up -d`

### Connection Issues:
1. Ensure container is running: `docker-compose ps`
2. Check logs: `docker-compose logs postgres`
3. Verify port mapping: `docker port course-db`

### Maven Issues:
1. Make Maven wrapper executable: `chmod +x mvnw`
2. Clean and rebuild: `./mvnw clean compile`
3. Skip tests if needed: `./mvnw spring-boot:run -DskipTests` 

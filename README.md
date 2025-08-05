# Course Management System

A Spring Boot application for managing courses with PostgreSQL database.

## Prerequisites

- Docker and Docker Compose installed
- Java 17 or higher
- Gradle

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
   ./gradlew build
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the application:**
   - URL: http://localhost:8080

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
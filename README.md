# 🎓 Course Management System

A comprehensive Spring Boot REST API for managing educational courses, instructors, students, and reviews. Built with modern Java practices, featuring a clean architecture, complete CRUD operations, and comprehensive testing.

## ✨ Features

- 🏗️ **Complete Course Management** - Courses, instructors, students, and reviews
- 🔗 **Rich Relationships** - One-to-one, one-to-many, and many-to-many mappings
- 🛡️ **Robust Validation** - Input validation with detailed error responses
- 📚 **Interactive API Docs** - Swagger/OpenAPI 3 documentation
- 🧪 **Comprehensive Testing** - Unit, integration, and web layer tests with 80%+ coverage
- 🐘 **PostgreSQL Integration** - Production-ready database with UUID primary keys
- 🏭 **Professional Architecture** - Layered design with DTOs, services, and repositories

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.8+

### 1. Start Database
```bash
docker-compose up -d
```

### 2. Run Application
```bash
./mvnw spring-boot:run
```

### 3. Explore APIs
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/actuator/health

## 📊 System Overview

### Core Entities
- **👨‍🏫 Instructors** - Manage instructor profiles and details
- **🎓 Courses** - Course catalog with instructor assignments
- **👨‍🎓 Students** - Student management and profiles
- **⭐ Reviews** - Course reviews and ratings system
- **📝 Enrollments** - Student course enrollment tracking

### API Endpoints Summary
```
📚 Courses      → /api/v1/courses/*
👨‍🏫 Instructors  → /api/v1/instructors/*
👨‍🎓 Students     → /api/v1/students/*
⭐ Reviews      → /api/v1/reviews/*
📝 Enrollments → /api/v1/enrollments/*
```

## 📖 Documentation

### 🎯 **Getting Started**
- **[Complete Setup Guide](./docs/guides/getting-started.md)** - Detailed setup, configuration, and first steps
- **[System Architecture](./docs/guides/architecture.md)** - Design patterns, relationships, and structure
- **[Database Schema](./docs/reference/database-schema.md)** - Entity relationships and database design

### 🔌 **API References**
- **[Instructors API](./docs/api/instructors.md)** - Instructor and instructor details management
- **[Students API](./docs/api/students.md)** - Student management and operations
- **[Courses API](./docs/api/courses.md)** - Course catalog and management
- **[Reviews API](./docs/api/reviews.md)** - Review system and ratings
- **[Enrollments API](./docs/api/enrollments.md)** - Student enrollment management

### 🧪 **Development & Testing**
- **[Testing Guide](./docs/guides/testing.md)** - Comprehensive testing strategy and examples
- **[Configuration Reference](./docs/reference/configuration.md)** - All configuration options and settings
- **[Troubleshooting](./docs/reference/troubleshooting.md)** - Common issues and solutions

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │────│    Services     │────│  Repositories   │
│  (REST Layer)   │    │ (Business Logic)│    │  (Data Access)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      DTOs       │    │    Entities     │    │   PostgreSQL    │
│ (API Contracts) │    │  (Domain Model) │    │   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Database**: PostgreSQL 16
- **ORM**: Spring Data JPA / Hibernate
- **Testing**: JUnit 5, Mockito, TestContainers
- **Documentation**: SpringDoc OpenAPI 3
- **Build**: Maven
- **Containerization**: Docker & Docker Compose

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

**Coverage Goals**: 80%+ instruction coverage, 70%+ branch coverage

## 🚀 Example Usage

### Create a Complete Course Setup
```bash
# 1. Create an instructor
curl -X POST http://localhost:8080/api/v1/instructors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "instructorDetails": {
      "youtubeChannel": "https://youtube.com/@johndoe",
      "hobby": "Teaching Java and Spring Boot"
    }
  }'

# 2. Create a course
curl -X POST http://localhost:8080/api/v1/courses \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Masterclass",
    "instructorId": "{instructor-id-from-step-1}"
  }'

# 3. Create a student and enroll
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.johnson@example.com"
  }'
```

> 💡 **Tip**: Use the [interactive Swagger UI](http://localhost:8080/swagger-ui.html) for easier API exploration!

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built with ❤️ for learning Spring Boot and modern Java development practices**

> 🌟 **Perfect for**: Portfolio projects, learning Spring Boot, understanding REST API design, and practicing modern Java development!
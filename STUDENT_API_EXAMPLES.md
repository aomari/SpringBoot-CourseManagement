# Student Management API Examples

This document provides comprehensive examples for using the Student Management API endpoints, including CRUD operations and enrollment management.

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
Currently, no authentication is required for these examples.

---

## Student CRUD Operations

### 1. Create Student

**POST** `/students`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "courses": null
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8080/api/v1/students" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com"
  }'
```

### 2. Get Student by ID (with courses)

**GET** `/students/{id}`

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "courses": [
    {
      "id": "456e7890-f12a-34b5-c678-901234567890",
      "title": "Introduction to Spring Boot",
      "instructorName": "Jane Smith"
    },
    {
      "id": "789a0123-4567-8901-b234-567890123456",
      "title": "Advanced Java Programming",
      "instructorName": "Bob Johnson"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000"
```

### 3. Get All Students

**GET** `/students`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "courses": null
  },
  {
    "id": "234e5678-f90a-12b3-c456-789012345678",
    "firstName": "Jane",
    "lastName": "Smith",
    "fullName": "Jane Smith",
    "email": "jane.smith@example.com",
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students"
```

### 4. Update Student

**PUT** `/students/{id}`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com"
}
```

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Smith",
  "fullName": "John Smith",
  "email": "john.smith@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T12:00:00",
  "courses": null
}
```

**cURL Example:**
```bash
curl -X PUT "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@example.com"
  }'
```

### 5. Delete Student

**DELETE** `/students/{id}`

**Response (204 No Content)**

**cURL Example:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000"
```

---

## Student Course Management

### 6. Get Student Courses

**GET** `/students/{id}/courses`

**Response (200 OK):**
```json
[
  {
    "id": "456e7890-f12a-34b5-c678-901234567890",
    "title": "Introduction to Spring Boot",
    "instructorName": "Jane Smith"
  },
  {
    "id": "789a0123-4567-8901-b234-567890123456",
    "title": "Advanced Java Programming",
    "instructorName": "Bob Johnson"
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/courses"
```

---

## Enrollment Operations

### 7. Enroll Student in Course

**POST** `/students/{id}/enroll`

**Request Body:**
```json
{
  "courseId": "456e7890-f12a-34b5-c678-901234567890"
}
```

**Response (200 OK)** - Empty body

**cURL Example:**
```bash
curl -X POST "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enroll" \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "456e7890-f12a-34b5-c678-901234567890"
  }'
```

### 8. Unenroll Student from Course

**DELETE** `/students/{id}/unenroll`

**Request Body:**
```json
{
  "courseId": "456e7890-f12a-34b5-c678-901234567890"
}
```

**Response (200 OK)** - Empty body

**cURL Example:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/unenroll" \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "456e7890-f12a-34b5-c678-901234567890"
  }'
```

### 9. Get Students Enrolled in Course

**GET** `/courses/{id}/students`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "courses": null
  },
  {
    "id": "234e5678-f90a-12b3-c456-789012345678",
    "firstName": "Jane",
    "lastName": "Smith",
    "fullName": "Jane Smith",
    "email": "jane.smith@example.com",
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/courses/456e7890-f12a-34b5-c678-901234567890/students"
```

### 10. Check Enrollment Status

**GET** `/students/{studentId}/enrollment/courses/{courseId}`

**Response (200 OK):**
```json
true
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enrollment/courses/456e7890-f12a-34b5-c678-901234567890"
```

### 11. Count Students in Course

**GET** `/courses/{id}/students/count`

**Response (200 OK):**
```json
5
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/courses/456e7890-f12a-34b5-c678-901234567890/students/count"
```

---

## Search Operations

### 12. Search Students by Name

**GET** `/students/search/name?name={searchTerm}`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/search/name?name=John"
```

### 13. Search Students by Email

**GET** `/students/search/email?email={searchTerm}`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/search/email?email=john"
```

### 14. Get Student by Email

**GET** `/students/email/{email}`

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "courses": null
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/email/john.doe@example.com"
```

### 15. Get Students with No Courses

**GET** `/students/no-courses`

**Response (200 OK):**
```json
[
  {
    "id": "345e6789-0123-4567-8901-234567890123",
    "firstName": "Bob",
    "lastName": "Wilson",
    "fullName": "Bob Wilson",
    "email": "bob.wilson@example.com",
    "createdAt": "2023-12-01T12:00:00",
    "updatedAt": "2023-12-01T12:00:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/students/no-courses"
```

### 16. Get Students by Instructor

**GET** `/instructors/{id}/students`

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "courses": null
  }
]
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/instructors/789a0123-4567-8901-b234-567890123456/students"
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2023-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "firstName": "First name is required",
    "email": "Email should be valid"
  },
  "path": "/api/v1/students"
}
```

### 404 Not Found
```json
{
  "timestamp": "2023-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/students/123e4567-e89b-12d3-a456-426614174000"
}
```

### 409 Conflict
```json
{
  "timestamp": "2023-12-01T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Student already exists with email: john.doe@example.com",
  "path": "/api/v1/students"
}
```

---

## Complete Workflow Example

Here's a complete workflow example demonstrating the typical usage of the Student API:

### 1. Create a new student
```bash
curl -X POST "http://localhost:8080/api/v1/students" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.johnson@example.com"
  }'
```

### 2. Get the student with courses (initially empty)
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000"
```

### 3. Enroll student in multiple courses
```bash
# Enroll in first course
curl -X POST "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enroll" \
  -H "Content-Type: application/json" \
  -d '{"courseId": "456e7890-f12a-34b5-c678-901234567890"}'

# Enroll in second course
curl -X POST "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enroll" \
  -H "Content-Type: application/json" \
  -d '{"courseId": "789a0123-4567-8901-b234-567890123456"}'
```

### 4. Get student with enrolled courses
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000"
```

### 5. Get only the student's courses
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/courses"
```

### 6. Check enrollment status
```bash
curl -X GET "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enrollment/courses/456e7890-f12a-34b5-c678-901234567890"
```

### 7. Unenroll from a course
```bash
curl -X DELETE "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/unenroll" \
  -H "Content-Type: application/json" \
  -d '{"courseId": "456e7890-f12a-34b5-c678-901234567890"}'
```

### 8. Update student information
```bash
curl -X PUT "http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson-Smith", 
    "email": "alice.johnson.smith@example.com"
  }'
```

### 9. Search for students
```bash
curl -X GET "http://localhost:8080/api/v1/students/search/name?name=Alice"
```

### 10. Get course enrollment statistics
```bash
curl -X GET "http://localhost:8080/api/v1/courses/789a0123-4567-8901-b234-567890123456/students/count"
```

---

## Testing with Swagger UI

Once the application is running, you can test all these endpoints interactively using Swagger UI:

**URL:** http://localhost:8080/swagger-ui.html

The Swagger UI provides:
- Interactive API documentation
- Request/response examples
- Parameter validation
- Direct testing capabilities
- Schema definitions

Look for the following tags in Swagger UI:
- **Student API** - All student CRUD operations
- **Enrollment API** - All enrollment management operations
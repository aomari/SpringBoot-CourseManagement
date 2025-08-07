# 👨‍🎓 Students API Reference

Complete API reference for managing students and their enrollments in the Course Management System.

## Overview

The Students API provides endpoints for managing student profiles and their course enrollments. Students can enroll in multiple courses and leave reviews for courses they've taken.

### Base URL
```
http://localhost:8080/api/v1
```

### Related Endpoints
- **Students**: `/students/*`
- **Enrollments**: `/enrollments/*` (see [Enrollments API](./enrollments.md))

## 👨‍🎓 Student Management

### Create Student

Create a new student profile.

**`POST /students`**

#### Request Body
```json
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com"
}
```

#### Response `201 Created`
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "Alice",
  "lastName": "Johnson",
  "fullName": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "enrollments": []
}
```

### Get All Students

Retrieve all students in the system.

**`GET /students`**

#### Response `200 OK`
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "Alice",
    "lastName": "Johnson",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "enrollments": [
      {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "courseId": "789e1234-e89b-12d3-a456-426614174002",
        "courseTitle": "Spring Boot Fundamentals",
        "enrolledAt": "2023-12-01T11:00:00"
      }
    ]
  },
  {
    "id": "789e1234-e89b-12d3-a456-426614174003",
    "firstName": "Bob",
    "lastName": "Wilson",
    "fullName": "Bob Wilson",
    "email": "bob.wilson@example.com",
    "createdAt": "2023-12-01T10:45:00",
    "updatedAt": "2023-12-01T10:45:00",
    "enrollments": []
  }
]
```

### Get Student by ID

Retrieve a specific student by their ID.

**`GET /students/{id}`**

#### Path Parameters
- `id` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "Alice",
  "lastName": "Johnson",
  "fullName": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "enrollments": [
    {
      "id": "456e7890-e89b-12d3-a456-426614174001",
      "courseId": "789e1234-e89b-12d3-a456-426614174002",
      "courseTitle": "Spring Boot Fundamentals",
      "enrolledAt": "2023-12-01T11:00:00"
    }
  ]
}
```

### Update Student

Update an existing student's information.

**`PUT /students/{id}`**

#### Path Parameters
- `id` (UUID) - The student's unique identifier

#### Request Body
```json
{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice.smith@example.com"
}
```

#### Response `200 OK`
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "Alice",
  "lastName": "Smith",
  "fullName": "Alice Smith",
  "email": "alice.smith@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T12:15:00",
  "enrollments": [
    {
      "id": "456e7890-e89b-12d3-a456-426614174001",
      "courseId": "789e1234-e89b-12d3-a456-426614174002",
      "courseTitle": "Spring Boot Fundamentals",
      "enrolledAt": "2023-12-01T11:00:00"
    }
  ]
}
```

### Delete Student

Delete a student and all their enrollments.

**`DELETE /students/{id}`**

#### Path Parameters
- `id` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
{
  "message": "Student deleted successfully",
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "deletedAt": "2023-12-01T12:30:00"
}
```

## 🔍 Search & Filter Operations

### Search by Name

Find students by first name, last name, or full name.

**`GET /students/search?name={name}`**

#### Query Parameters
- `name` (string) - Search term for student names

#### Example
```bash
GET /students/search?name=Alice
```

#### Response `200 OK`
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "Alice",
    "lastName": "Johnson",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "enrollments": []
  }
]
```

### Get by Email

Find a student by their email address.

**`GET /students/email/{email}`**

#### Path Parameters
- `email` (string) - The student's email address

#### Example
```bash
GET /students/email/alice.johnson@example.com
```

### Get Students with Enrollments

Retrieve only students who are enrolled in at least one course.

**`GET /students/with-enrollments`**

### Get Students without Enrollments

Retrieve only students who are not enrolled in any courses.

**`GET /students/without-enrollments`**

## 📚 Course Enrollment Operations

### Enroll Student in Course

Enroll a student in a specific course.

**`POST /students/{studentId}/enroll/{courseId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `201 Created`
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "courseId": "789e1234-e89b-12d3-a456-426614174002",
  "enrolledAt": "2023-12-01T11:00:00",
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com"
  },
  "course": {
    "id": "789e1234-e89b-12d3-a456-426614174002",
    "title": "Spring Boot Fundamentals"
  }
}
```

### Unenroll Student from Course

Remove a student's enrollment from a specific course.

**`DELETE /students/{studentId}/unenroll/{courseId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "message": "Student unenrolled successfully",
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "courseId": "789e1234-e89b-12d3-a456-426614174002",
  "unenrolledAt": "2023-12-01T12:45:00"
}
```

### Get Student's Enrollments

Get all courses a student is enrolled in.

**`GET /students/{studentId}/enrollments`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "courseId": "789e1234-e89b-12d3-a456-426614174002",
    "courseTitle": "Spring Boot Fundamentals",
    "enrolledAt": "2023-12-01T11:00:00",
    "instructor": {
      "id": "abc1234e-e89b-12d3-a456-426614174000",
      "fullName": "John Doe"
    }
  },
  {
    "id": "def5678f-e89b-12d3-a456-426614174003",
    "courseId": "ghi9012g-e89b-12d3-a456-426614174004",
    "courseTitle": "Advanced Spring Security",
    "enrolledAt": "2023-12-02T09:30:00",
    "instructor": {
      "id": "jkl3456h-e89b-12d3-a456-426614174005",
      "fullName": "Jane Smith"
    }
  }
]
```

### Get Student's Course Count

Get the number of courses a student is enrolled in.

**`GET /students/{studentId}/course-count`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "enrollmentCount": 2
}
```

## ✅ Validation & Existence Checks

### Check if Student Exists by ID

**`GET /students/{id}/exists`**

#### Response `200 OK`
```json
true
```

### Check if Student Exists by Email

**`GET /students/email/{email}/exists`**

#### Response `200 OK`
```json
false
```

### Check Enrollment Status

Check if a student is enrolled in a specific course.

**`GET /students/{studentId}/enrolled/{courseId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "courseId": "789e1234-e89b-12d3-a456-426614174002",
  "isEnrolled": true,
  "enrolledAt": "2023-12-01T11:00:00"
}
```

## 🚨 Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Student not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/students/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 400 Validation Error
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/students",
  "timestamp": "2023-12-01T10:30:00",
  "validationErrors": [
    {
      "field": "email",
      "rejectedValue": "invalid-email",
      "message": "Email should be valid"
    },
    {
      "field": "firstName",
      "rejectedValue": "",
      "message": "First name is required"
    }
  ]
}
```

### 409 Conflict (Already Exists)
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Student already exists with email: alice.johnson@example.com",
  "path": "/api/v1/students",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 409 Already Enrolled
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Student is already enrolled in this course",
  "path": "/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enroll/789e1234-e89b-12d3-a456-426614174002",
  "timestamp": "2023-12-01T11:30:00"
}
```

## 🔧 cURL Examples

### Create Student
```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Bob",
    "lastName": "Wilson",
    "email": "bob.wilson@example.com"
  }'
```

### Enroll Student in Course
```bash
curl -X POST http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enroll/789e1234-e89b-12d3-a456-426614174002 \
  -H "Content-Type: application/json"
```

### Search Students by Name
```bash
curl -X GET "http://localhost:8080/api/v1/students/search?name=Alice" \
  -H "Accept: application/json"
```

### Get Student's Enrollments
```bash
curl -X GET http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/enrollments \
  -H "Accept: application/json"
```

### Unenroll Student from Course
```bash
curl -X DELETE http://localhost:8080/api/v1/students/123e4567-e89b-12d3-a456-426614174000/unenroll/789e1234-e89b-12d3-a456-426614174002
```

---

## 📚 Related Documentation

- **[Courses API](./courses.md)** - Course management operations
- **[Enrollments API](./enrollments.md)** - Detailed enrollment management
- **[Reviews API](./reviews.md)** - Student course reviews
- **[Getting Started Guide](../guides/getting-started.md)** - Setup and configuration
- **[Database Schema](../reference/database-schema.md)** - Entity relationships

---

*For interactive testing, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)*
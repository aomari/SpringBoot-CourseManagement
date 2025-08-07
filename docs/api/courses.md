# 📚 Courses API Reference

Complete API reference for managing courses in the Course Management System.

## Overview

The Courses API provides endpoints for managing the course catalog, including course creation, updates, and relationship management with instructors, students, and reviews.

### Base URL
```
http://localhost:8080/api/v1
```

### Related Endpoints
- **Courses**: `/courses/*`
- **Instructors**: `/instructors/*` (see [Instructors API](./instructors.md))
- **Students**: `/students/*` (see [Students API](./students.md))
- **Reviews**: `/reviews/*` (see [Reviews API](./reviews.md))

## 📚 Course Management

### Create Course

Create a new course with an assigned instructor.

**`POST /courses`**

#### Request Body
```json
{
  "title": "Spring Boot Fundamentals",
  "instructorId": "123e4567-e89b-12d3-a456-426614174000"
}
```

#### Response `201 Created`
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Spring Boot Fundamentals",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com"
  },
  "reviews": [],
  "enrollmentCount": 0
}
```

### Get All Courses

Retrieve all courses in the system.

**`GET /courses`**

#### Response `200 OK`
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "instructor": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "fullName": "John Doe",
      "email": "john.doe@example.com"
    },
    "reviews": [
      {
        "id": "789e1234-e89b-12d3-a456-426614174002",
        "comment": "Excellent course! Very well structured.",
        "rating": 5,
        "studentName": "Alice Johnson",
        "createdAt": "2023-12-01T14:30:00"
      }
    ],
    "enrollmentCount": 3,
    "averageRating": 4.7
  },
  {
    "id": "abc1234e-e89b-12d3-a456-426614174003",
    "title": "Advanced Spring Security",
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00",
    "instructor": {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "fullName": "Jane Smith",
      "email": "jane.smith@example.com"
    },
    "reviews": [],
    "enrollmentCount": 1,
    "averageRating": null
  }
]
```

### Get Course by ID

Retrieve a specific course by its ID, including reviews and enrollment information.

**`GET /courses/{id}`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Spring Boot Fundamentals",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "instructorDetails": {
      "youtubeChannel": "https://youtube.com/@johndoe",
      "hoppy": "Teaching Java and Spring Boot"
    }
  },
  "reviews": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "comment": "Excellent course! Very well structured and easy to follow.",
      "rating": 5,
      "student": {
        "id": "ghi9012g-e89b-12d3-a456-426614174005",
        "fullName": "Alice Johnson",
        "email": "alice.johnson@example.com"
      },
      "createdAt": "2023-12-01T14:30:00"
    },
    {
      "id": "jkl3456h-e89b-12d3-a456-426614174006",
      "comment": "Great practical examples. Learned a lot!",
      "rating": 4,
      "student": {
        "id": "mno7890i-e89b-12d3-a456-426614174007",
        "fullName": "Bob Wilson",
        "email": "bob.wilson@example.com"
      },
      "createdAt": "2023-12-01T16:15:00"
    }
  ],
  "enrollmentCount": 3,
  "averageRating": 4.5
}
```

### Update Course

Update an existing course's information.

**`PUT /courses/{id}`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Request Body
```json
{
  "title": "Spring Boot Fundamentals - Updated Edition",
  "instructorId": "123e4567-e89b-12d3-a456-426614174000"
}
```

#### Response `200 OK`
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Spring Boot Fundamentals - Updated Edition",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T15:45:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com"
  },
  "reviews": [],
  "enrollmentCount": 3
}
```

### Delete Course

Delete a course and all its associated reviews and enrollments.

**`DELETE /courses/{id}`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "message": "Course deleted successfully",
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "deletedAt": "2023-12-01T16:00:00"
}
```

## 🔍 Search & Filter Operations

### Search by Title

Find courses by title using partial matching.

**`GET /courses/search?title={title}`**

#### Query Parameters
- `title` (string) - Search term for course titles

#### Example
```bash
GET /courses/search?title=Spring
```

#### Response `200 OK`
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "instructor": {
      "fullName": "John Doe"
    },
    "enrollmentCount": 3,
    "averageRating": 4.5
  },
  {
    "id": "abc1234e-e89b-12d3-a456-426614174003",
    "title": "Advanced Spring Security",
    "instructor": {
      "fullName": "Jane Smith"
    },
    "enrollmentCount": 1,
    "averageRating": null
  }
]
```

### Get Courses by Instructor

Get all courses taught by a specific instructor.

**`GET /courses/instructor/{instructorId}`**

#### Path Parameters
- `instructorId` (UUID) - The instructor's unique identifier

#### Example
```bash
GET /courses/instructor/123e4567-e89b-12d3-a456-426614174000
```

### Get Courses with Reviews

Retrieve only courses that have at least one review.

**`GET /courses/with-reviews`**

### Get Courses without Reviews

Retrieve only courses that have no reviews yet.

**`GET /courses/without-reviews`**

### Get Popular Courses

Get courses ordered by enrollment count (most popular first).

**`GET /courses/popular`**

#### Query Parameters (Optional)
- `limit` (integer) - Maximum number of courses to return (default: 10)

#### Example
```bash
GET /courses/popular?limit=5
```

### Get Top Rated Courses

Get courses ordered by average rating (highest rated first).

**`GET /courses/top-rated`**

#### Query Parameters (Optional)
- `limit` (integer) - Maximum number of courses to return (default: 10)
- `minReviews` (integer) - Minimum number of reviews required (default: 1)

#### Example
```bash
GET /courses/top-rated?limit=5&minReviews=3
```

## 📊 Course Statistics

### Get Course Statistics

Get detailed statistics for a specific course.

**`GET /courses/{id}/stats`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "courseTitle": "Spring Boot Fundamentals",
  "enrollmentCount": 5,
  "reviewCount": 3,
  "averageRating": 4.3,
  "ratingDistribution": {
    "5": 1,
    "4": 2,
    "3": 0,
    "2": 0,
    "1": 0
  },
  "createdAt": "2023-12-01T10:30:00",
  "lastEnrollment": "2023-12-02T14:20:00",
  "lastReview": "2023-12-01T16:15:00"
}
```

### Get Enrollment Count

Get the number of students enrolled in a course.

**`GET /courses/{id}/enrollment-count`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "enrollmentCount": 5
}
```

### Get Review Summary

Get a summary of reviews for a course.

**`GET /courses/{id}/review-summary`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "reviewCount": 3,
  "averageRating": 4.3,
  "ratingDistribution": {
    "5": 1,
    "4": 2,
    "3": 0,
    "2": 0,
    "1": 0
  },
  "latestReviews": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "comment": "Excellent course!",
      "rating": 5,
      "studentName": "Alice Johnson",
      "createdAt": "2023-12-01T14:30:00"
    }
  ]
}
```

## 👨‍🎓 Student Management for Courses

### Get Course Enrollments

Get all students enrolled in a specific course.

**`GET /courses/{id}/enrollments`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
[
  {
    "id": "abc1234e-e89b-12d3-a456-426614174003",
    "student": {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "fullName": "Alice Johnson",
      "email": "alice.johnson@example.com"
    },
    "enrolledAt": "2023-12-01T11:00:00"
  },
  {
    "id": "ghi9012g-e89b-12d3-a456-426614174005",
    "student": {
      "id": "jkl3456h-e89b-12d3-a456-426614174006",
      "fullName": "Bob Wilson",
      "email": "bob.wilson@example.com"
    },
    "enrolledAt": "2023-12-01T13:30:00"
  }
]
```

### Get Course Reviews

Get all reviews for a specific course.

**`GET /courses/{id}/reviews`**

#### Path Parameters
- `id` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
[
  {
    "id": "789e1234-e89b-12d3-a456-426614174002",
    "comment": "Excellent course! Very well structured and easy to follow.",
    "rating": 5,
    "student": {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "fullName": "Alice Johnson",
      "email": "alice.johnson@example.com"
    },
    "createdAt": "2023-12-01T14:30:00",
    "updatedAt": "2023-12-01T14:30:00"
  },
  {
    "id": "mno7890i-e89b-12d3-a456-426614174007",
    "comment": "Great practical examples. Learned a lot!",
    "rating": 4,
    "student": {
      "id": "jkl3456h-e89b-12d3-a456-426614174006",
      "fullName": "Bob Wilson",
      "email": "bob.wilson@example.com"
    },
    "createdAt": "2023-12-01T16:15:00",
    "updatedAt": "2023-12-01T16:15:00"
  }
]
```

## ✅ Validation & Existence Checks

### Check if Course Exists

**`GET /courses/{id}/exists`**

#### Response `200 OK`
```json
true
```

### Check if Course has Students

**`GET /courses/{id}/has-students`**

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "hasStudents": true,
  "enrollmentCount": 5
}
```

### Check if Course has Reviews

**`GET /courses/{id}/has-reviews`**

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "hasReviews": true,
  "reviewCount": 3
}
```

## 🚨 Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Course not found with id: 456e7890-e89b-12d3-a456-426614174001",
  "path": "/api/v1/courses/456e7890-e89b-12d3-a456-426614174001",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 400 Validation Error
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/courses",
  "timestamp": "2023-12-01T10:30:00",
  "validationErrors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "Course title is required"
    },
    {
      "field": "instructorId",
      "rejectedValue": null,
      "message": "Instructor ID is required"
    }
  ]
}
```

### 409 Conflict (Already Exists)
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Course with title 'Spring Boot Fundamentals' already exists for this instructor",
  "path": "/api/v1/courses",
  "timestamp": "2023-12-01T10:30:00"
}
```

## 🔧 cURL Examples

### Create Course
```bash
curl -X POST http://localhost:8080/api/v1/courses \
  -H "Content-Type: application/json" \
  -d '{
    "title": "React Fundamentals",
    "instructorId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

### Search Courses by Title
```bash
curl -X GET "http://localhost:8080/api/v1/courses/search?title=Spring" \
  -H "Accept: application/json"
```

### Get Course Statistics
```bash
curl -X GET http://localhost:8080/api/v1/courses/456e7890-e89b-12d3-a456-426614174001/stats \
  -H "Accept: application/json"
```

### Get Popular Courses
```bash
curl -X GET "http://localhost:8080/api/v1/courses/popular?limit=5" \
  -H "Accept: application/json"
```

### Update Course
```bash
curl -X PUT http://localhost:8080/api/v1/courses/456e7890-e89b-12d3-a456-426614174001 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Advanced Topics",
    "instructorId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

---

## 📚 Related Documentation

- **[Instructors API](./instructors.md)** - Instructor management operations
- **[Students API](./students.md)** - Student management operations
- **[Reviews API](./reviews.md)** - Course review system
- **[Enrollments API](./enrollments.md)** - Enrollment management
- **[Getting Started Guide](../guides/getting-started.md)** - Setup and configuration
- **[Database Schema](../reference/database-schema.md)** - Entity relationships

---

*For interactive testing, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)*
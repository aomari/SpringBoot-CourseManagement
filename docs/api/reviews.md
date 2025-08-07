# ⭐ Reviews API Reference

Complete API reference for managing course reviews and ratings in the Course Management System.

## Overview

The Reviews API provides endpoints for managing student reviews of courses, including ratings, comments, and review statistics. Students can only review courses they are enrolled in.

### Base URL
```
http://localhost:8080/api/v1
```

### Related Endpoints
- **Reviews**: `/reviews/*`
- **Courses**: `/courses/*` (see [Courses API](./courses.md))
- **Students**: `/students/*` (see [Students API](./students.md))

## ⭐ Review Management

### Create Review

Create a new review for a course. Students can only review courses they are enrolled in.

**`POST /reviews`**

#### Request Body
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "comment": "Excellent course! Very well structured and easy to follow.",
  "rating": 5
}
```

#### Response `201 Created`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "comment": "Excellent course! Very well structured and easy to follow.",
  "rating": 5,
  "createdAt": "2023-12-01T14:30:00",
  "updatedAt": "2023-12-01T14:30:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "instructor": {
      "id": "abc1234e-e89b-12d3-a456-426614174003",
      "fullName": "John Doe"
    }
  },
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com"
  }
}
```

### Get All Reviews

Retrieve all reviews in the system.

**`GET /reviews`**

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)
- `sort` (string) - Sort criteria (default: "createdAt,desc")

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "comment": "Excellent course! Very well structured and easy to follow.",
      "rating": 5,
      "createdAt": "2023-12-01T14:30:00",
      "updatedAt": "2023-12-01T14:30:00",
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals"
      },
      "student": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "fullName": "Alice Johnson"
      }
    },
    {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "comment": "Great practical examples. Learned a lot!",
      "rating": 4,
      "createdAt": "2023-12-01T16:15:00",
      "updatedAt": "2023-12-01T16:15:00",
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals"
      },
      "student": {
        "id": "ghi9012g-e89b-12d3-a456-426614174005",
        "fullName": "Bob Wilson"
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "orderBy": "createdAt",
      "direction": "desc"
    }
  },
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### Get Review by ID

Retrieve a specific review by its ID.

**`GET /reviews/{id}`**

#### Path Parameters
- `id` (UUID) - The review's unique identifier

#### Response `200 OK`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "comment": "Excellent course! Very well structured and easy to follow.",
  "rating": 5,
  "createdAt": "2023-12-01T14:30:00",
  "updatedAt": "2023-12-01T14:30:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "instructor": {
      "id": "abc1234e-e89b-12d3-a456-426614174003",
      "fullName": "John Doe",
      "email": "john.doe@example.com"
    }
  },
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com"
  }
}
```

### Update Review

Update an existing review. Students can only update their own reviews.

**`PUT /reviews/{id}`**

#### Path Parameters
- `id` (UUID) - The review's unique identifier

#### Request Body
```json
{
  "comment": "Excellent course! Very well structured and easy to follow. Updated with more details.",
  "rating": 5
}
```

#### Response `200 OK`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "comment": "Excellent course! Very well structured and easy to follow. Updated with more details.",
  "rating": 5,
  "createdAt": "2023-12-01T14:30:00",
  "updatedAt": "2023-12-01T17:45:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals"
  },
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson"
  }
}
```

### Delete Review

Delete a review. Students can only delete their own reviews.

**`DELETE /reviews/{id}`**

#### Path Parameters
- `id` (UUID) - The review's unique identifier

#### Response `200 OK`
```json
{
  "message": "Review deleted successfully",
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "deletedAt": "2023-12-01T18:00:00"
}
```

## 🔍 Search & Filter Operations

### Get Reviews by Course

Get all reviews for a specific course.

**`GET /reviews/course/{courseId}`**

#### Path Parameters
- `courseId` (UUID) - The course's unique identifier

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)
- `sort` (string) - Sort criteria (default: "createdAt,desc")

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "comment": "Excellent course!",
      "rating": 5,
      "student": {
        "fullName": "Alice Johnson"
      },
      "createdAt": "2023-12-01T14:30:00"
    }
  ],
  "totalElements": 1,
  "courseInfo": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "averageRating": 4.5,
    "reviewCount": 3
  }
}
```

### Get Reviews by Student

Get all reviews written by a specific student.

**`GET /reviews/student/{studentId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "comment": "Excellent course!",
      "rating": 5,
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals"
      },
      "createdAt": "2023-12-01T14:30:00"
    }
  ],
  "totalElements": 1,
  "studentInfo": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "reviewCount": 3
  }
}
```

### Get Reviews by Rating

Filter reviews by rating value.

**`GET /reviews/rating/{rating}`**

#### Path Parameters
- `rating` (integer) - Rating value (1-5)

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)

#### Example
```bash
GET /reviews/rating/5
```

### Search Reviews by Comment

Search reviews by comment content.

**`GET /reviews/search?comment={searchTerm}`**

#### Query Parameters
- `comment` (string) - Search term for review comments
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)

#### Example
```bash
GET /reviews/search?comment=excellent
```

## 📊 Review Statistics

### Get Review Statistics for Course

Get detailed review statistics for a specific course.

**`GET /reviews/course/{courseId}/stats`**

#### Path Parameters
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "courseTitle": "Spring Boot Fundamentals",
  "reviewCount": 5,
  "averageRating": 4.4,
  "ratingDistribution": {
    "5": 2,
    "4": 2,
    "3": 1,
    "2": 0,
    "1": 0
  },
  "latestReview": "2023-12-02T10:30:00",
  "oldestReview": "2023-12-01T14:30:00"
}
```

### Get Student Review Statistics

Get review statistics for a specific student.

**`GET /reviews/student/{studentId}/stats`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "studentName": "Alice Johnson",
  "reviewCount": 3,
  "averageRatingGiven": 4.3,
  "coursesReviewed": 3,
  "latestReview": "2023-12-02T10:30:00",
  "firstReview": "2023-12-01T14:30:00"
}
```

### Get Overall Review Statistics

Get system-wide review statistics.

**`GET /reviews/stats`**

#### Response `200 OK`
```json
{
  "totalReviews": 45,
  "totalCourses": 12,
  "totalStudents": 25,
  "overallAverageRating": 4.2,
  "ratingDistribution": {
    "5": 18,
    "4": 15,
    "3": 8,
    "2": 3,
    "1": 1
  },
  "reviewsThisMonth": 12,
  "reviewsThisWeek": 3
}
```

## ✅ Validation & Existence Checks

### Check if Review Exists

**`GET /reviews/{id}/exists`**

#### Response `200 OK`
```json
true
```

### Check if Student Can Review Course

Check if a student is eligible to review a course (must be enrolled).

**`GET /reviews/can-review?studentId={studentId}&courseId={courseId}`**

#### Query Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "canReview": true,
  "reason": "Student is enrolled in the course",
  "isEnrolled": true,
  "hasExistingReview": false
}
```

#### Response `200 OK` (Cannot Review)
```json
{
  "canReview": false,
  "reason": "Student already has a review for this course",
  "isEnrolled": true,
  "hasExistingReview": true,
  "existingReviewId": "789e1234-e89b-12d3-a456-426614174002"
}
```

### Check if Student Has Reviewed Course

**`GET /reviews/exists?studentId={studentId}&courseId={courseId}`**

#### Query Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "exists": true,
  "reviewId": "789e1234-e89b-12d3-a456-426614174002",
  "rating": 5,
  "createdAt": "2023-12-01T14:30:00"
}
```

## 🚨 Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Review not found with id: 789e1234-e89b-12d3-a456-426614174002",
  "path": "/api/v1/reviews/789e1234-e89b-12d3-a456-426614174002",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 400 Validation Error
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/reviews",
  "timestamp": "2023-12-01T10:30:00",
  "validationErrors": [
    {
      "field": "rating",
      "rejectedValue": 6,
      "message": "Rating must be between 1 and 5"
    },
    {
      "field": "comment",
      "rejectedValue": "",
      "message": "Review comment is required"
    }
  ]
}
```

### 409 Conflict (Already Exists)
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Student has already reviewed this course",
  "path": "/api/v1/reviews",
  "timestamp": "2023-12-01T10:30:00",
  "details": {
    "studentId": "123e4567-e89b-12d3-a456-426614174000",
    "courseId": "456e7890-e89b-12d3-a456-426614174001",
    "existingReviewId": "789e1234-e89b-12d3-a456-426614174002"
  }
}
```

### 403 Forbidden (Not Enrolled)
```json
{
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Student is not enrolled in this course and cannot leave a review",
  "path": "/api/v1/reviews",
  "timestamp": "2023-12-01T10:30:00",
  "details": {
    "studentId": "123e4567-e89b-12d3-a456-426614174000",
    "courseId": "456e7890-e89b-12d3-a456-426614174001"
  }
}
```

## 🔧 cURL Examples

### Create Review
```bash
curl -X POST http://localhost:8080/api/v1/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "456e7890-e89b-12d3-a456-426614174001",
    "studentId": "123e4567-e89b-12d3-a456-426614174000",
    "comment": "Amazing course! Highly recommend it.",
    "rating": 5
  }'
```

### Get Reviews for Course
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/course/456e7890-e89b-12d3-a456-426614174001?page=0&size=10" \
  -H "Accept: application/json"
```

### Update Review
```bash
curl -X PUT http://localhost:8080/api/v1/reviews/789e1234-e89b-12d3-a456-426614174002 \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Updated review: Still an excellent course!",
    "rating": 5
  }'
```

### Get Course Review Statistics
```bash
curl -X GET http://localhost:8080/api/v1/reviews/course/456e7890-e89b-12d3-a456-426614174001/stats \
  -H "Accept: application/json"
```

### Search Reviews by Comment
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/search?comment=excellent" \
  -H "Accept: application/json"
```

### Check if Student Can Review Course
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/can-review?studentId=123e4567-e89b-12d3-a456-426614174000&courseId=456e7890-e89b-12d3-a456-426614174001" \
  -H "Accept: application/json"
```

---

## 📚 Related Documentation

- **[Courses API](./courses.md)** - Course management operations
- **[Students API](./students.md)** - Student management operations
- **[Enrollments API](./enrollments.md)** - Enrollment management
- **[Getting Started Guide](../guides/getting-started.md)** - Setup and configuration
- **[Database Schema](../reference/database-schema.md)** - Entity relationships

---

*For interactive testing, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)*
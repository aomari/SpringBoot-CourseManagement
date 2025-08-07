# 📝 Enrollments API Reference

Complete API reference for managing student course enrollments in the Course Management System.

## Overview

The Enrollments API provides endpoints for managing the relationship between students and courses, tracking enrollment dates, and managing enrollment lifecycle.

### Base URL
```
http://localhost:8080/api/v1
```

### Related Endpoints
- **Enrollments**: `/enrollments/*`
- **Students**: `/students/*` (see [Students API](./students.md))
- **Courses**: `/courses/*` (see [Courses API](./courses.md))

## 📝 Enrollment Management

### Create Enrollment

Enroll a student in a course.

**`POST /enrollments`**

#### Request Body
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "courseId": "456e7890-e89b-12d3-a456-426614174001"
}
```

#### Response `201 Created`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "enrolledAt": "2023-12-01T11:00:00",
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "Alice",
    "lastName": "Johnson",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com"
  },
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "instructor": {
      "id": "abc1234e-e89b-12d3-a456-426614174003",
      "fullName": "John Doe"
    }
  }
}
```

### Get All Enrollments

Retrieve all enrollments in the system.

**`GET /enrollments`**

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)
- `sort` (string) - Sort criteria (default: "enrolledAt,desc")

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "enrolledAt": "2023-12-01T11:00:00",
      "student": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "fullName": "Alice Johnson",
        "email": "alice.johnson@example.com"
      },
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals",
        "instructor": {
          "fullName": "John Doe"
        }
      }
    },
    {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "enrolledAt": "2023-12-01T13:30:00",
      "student": {
        "id": "ghi9012g-e89b-12d3-a456-426614174005",
        "fullName": "Bob Wilson",
        "email": "bob.wilson@example.com"
      },
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals",
        "instructor": {
          "fullName": "John Doe"
        }
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "orderBy": "enrolledAt",
      "direction": "desc"
    }
  },
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### Get Enrollment by ID

Retrieve a specific enrollment by its ID.

**`GET /enrollments/{id}`**

#### Path Parameters
- `id` (UUID) - The enrollment's unique identifier

#### Response `200 OK`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "enrolledAt": "2023-12-01T11:00:00",
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "Alice",
    "lastName": "Johnson",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "createdAt": "2023-11-30T10:00:00"
  },
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "createdAt": "2023-11-29T15:30:00",
    "instructor": {
      "id": "abc1234e-e89b-12d3-a456-426614174003",
      "fullName": "John Doe",
      "email": "john.doe@example.com"
    }
  }
}
```

### Delete Enrollment

Remove a student's enrollment from a course.

**`DELETE /enrollments/{id}`**

#### Path Parameters
- `id` (UUID) - The enrollment's unique identifier

#### Response `200 OK`
```json
{
  "message": "Enrollment removed successfully",
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "unenrolledAt": "2023-12-01T16:30:00"
}
```

## 🔍 Search & Filter Operations

### Get Enrollments by Student

Get all enrollments for a specific student.

**`GET /enrollments/student/{studentId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)
- `sort` (string) - Sort criteria (default: "enrolledAt,desc")

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "enrolledAt": "2023-12-01T11:00:00",
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Spring Boot Fundamentals",
        "instructor": {
          "fullName": "John Doe"
        }
      }
    },
    {
      "id": "jkl3456h-e89b-12d3-a456-426614174006",
      "enrolledAt": "2023-12-02T09:15:00",
      "course": {
        "id": "mno7890i-e89b-12d3-a456-426614174007",
        "title": "Advanced Spring Security",
        "instructor": {
          "fullName": "Jane Smith"
        }
      }
    }
  ],
  "totalElements": 2,
  "studentInfo": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "enrollmentCount": 2
  }
}
```

### Get Enrollments by Course

Get all enrollments for a specific course.

**`GET /enrollments/course/{courseId}`**

#### Path Parameters
- `courseId` (UUID) - The course's unique identifier

#### Query Parameters (Optional)
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)
- `sort` (string) - Sort criteria (default: "enrolledAt,desc")

#### Response `200 OK`
```json
{
  "content": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "enrolledAt": "2023-12-01T11:00:00",
      "student": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "fullName": "Alice Johnson",
        "email": "alice.johnson@example.com"
      }
    },
    {
      "id": "def5678f-e89b-12d3-a456-426614174004",
      "enrolledAt": "2023-12-01T13:30:00",
      "student": {
        "id": "ghi9012g-e89b-12d3-a456-426614174005",
        "fullName": "Bob Wilson",
        "email": "bob.wilson@example.com"
      }
    }
  ],
  "totalElements": 2,
  "courseInfo": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "instructor": {
      "fullName": "John Doe"
    },
    "enrollmentCount": 2
  }
}
```

### Get Enrollments by Date Range

Get enrollments within a specific date range.

**`GET /enrollments/date-range?startDate={startDate}&endDate={endDate}`**

#### Query Parameters
- `startDate` (string) - Start date in ISO format (e.g., "2023-12-01T00:00:00")
- `endDate` (string) - End date in ISO format (e.g., "2023-12-31T23:59:59")
- `page` (integer) - Page number (default: 0)
- `size` (integer) - Page size (default: 20)

#### Example
```bash
GET /enrollments/date-range?startDate=2023-12-01T00:00:00&endDate=2023-12-31T23:59:59
```

### Get Recent Enrollments

Get the most recent enrollments.

**`GET /enrollments/recent`**

#### Query Parameters (Optional)
- `limit` (integer) - Maximum number of enrollments to return (default: 10)

#### Response `200 OK`
```json
[
  {
    "id": "789e1234-e89b-12d3-a456-426614174002",
    "enrolledAt": "2023-12-01T11:00:00",
    "student": {
      "fullName": "Alice Johnson"
    },
    "course": {
      "title": "Spring Boot Fundamentals",
      "instructor": {
        "fullName": "John Doe"
      }
    }
  }
]
```

## 📊 Enrollment Statistics

### Get Enrollment Count for Course

Get the total number of students enrolled in a course.

**`GET /enrollments/course/{courseId}/count`**

#### Path Parameters
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "courseTitle": "Spring Boot Fundamentals",
  "enrollmentCount": 5
}
```

### Get Enrollment Count for Student

Get the total number of courses a student is enrolled in.

**`GET /enrollments/student/{studentId}/count`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Response `200 OK`
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "studentName": "Alice Johnson",
  "enrollmentCount": 3
}
```

### Get Enrollment Statistics

Get overall enrollment statistics.

**`GET /enrollments/stats`**

#### Response `200 OK`
```json
{
  "totalEnrollments": 45,
  "totalStudents": 25,
  "totalCourses": 12,
  "averageEnrollmentsPerStudent": 1.8,
  "averageEnrollmentsPerCourse": 3.75,
  "enrollmentsThisMonth": 12,
  "enrollmentsThisWeek": 3,
  "enrollmentsToday": 1,
  "mostPopularCourse": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals",
    "enrollmentCount": 8
  },
  "mostActiveStudent": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson",
    "enrollmentCount": 5
  }
}
```

## 🔗 Bulk Operations

### Bulk Enroll Student

Enroll a student in multiple courses at once.

**`POST /enrollments/bulk/student/{studentId}`**

#### Path Parameters
- `studentId` (UUID) - The student's unique identifier

#### Request Body
```json
{
  "courseIds": [
    "456e7890-e89b-12d3-a456-426614174001",
    "abc1234e-e89b-12d3-a456-426614174003",
    "def5678f-e89b-12d3-a456-426614174004"
  ]
}
```

#### Response `201 Created`
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "studentName": "Alice Johnson",
  "enrollments": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "courseId": "456e7890-e89b-12d3-a456-426614174001",
      "courseTitle": "Spring Boot Fundamentals",
      "enrolledAt": "2023-12-01T11:00:00"
    },
    {
      "id": "ghi9012g-e89b-12d3-a456-426614174005",
      "courseId": "abc1234e-e89b-12d3-a456-426614174003",
      "courseTitle": "React Fundamentals",
      "enrolledAt": "2023-12-01T11:00:00"
    }
  ],
  "successCount": 2,
  "failedCount": 1,
  "errors": [
    {
      "courseId": "def5678f-e89b-12d3-a456-426614174004",
      "error": "Student is already enrolled in this course"
    }
  ]
}
```

### Bulk Enroll Course

Enroll multiple students in a single course.

**`POST /enrollments/bulk/course/{courseId}`**

#### Path Parameters
- `courseId` (UUID) - The course's unique identifier

#### Request Body
```json
{
  "studentIds": [
    "123e4567-e89b-12d3-a456-426614174000",
    "jkl3456h-e89b-12d3-a456-426614174006",
    "mno7890i-e89b-12d3-a456-426614174007"
  ]
}
```

#### Response `201 Created`
```json
{
  "courseId": "456e7890-e89b-12d3-a456-426614174001",
  "courseTitle": "Spring Boot Fundamentals",
  "enrollments": [
    {
      "id": "789e1234-e89b-12d3-a456-426614174002",
      "studentId": "123e4567-e89b-12d3-a456-426614174000",
      "studentName": "Alice Johnson",
      "enrolledAt": "2023-12-01T11:00:00"
    },
    {
      "id": "pqr1234j-e89b-12d3-a456-426614174008",
      "studentId": "jkl3456h-e89b-12d3-a456-426614174006",
      "studentName": "Bob Wilson",
      "enrolledAt": "2023-12-01T11:00:00"
    }
  ],
  "successCount": 2,
  "failedCount": 1,
  "errors": [
    {
      "studentId": "mno7890i-e89b-12d3-a456-426614174007",
      "error": "Student not found"
    }
  ]
}
```

## ✅ Validation & Existence Checks

### Check if Enrollment Exists

**`GET /enrollments/{id}/exists`**

#### Response `200 OK`
```json
true
```

### Check if Student is Enrolled in Course

**`GET /enrollments/exists?studentId={studentId}&courseId={courseId}`**

#### Query Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "exists": true,
  "enrollmentId": "789e1234-e89b-12d3-a456-426614174002",
  "enrolledAt": "2023-12-01T11:00:00"
}
```

### Get Enrollment by Student and Course

Get the enrollment record for a specific student-course combination.

**`GET /enrollments/find?studentId={studentId}&courseId={courseId}`**

#### Query Parameters
- `studentId` (UUID) - The student's unique identifier
- `courseId` (UUID) - The course's unique identifier

#### Response `200 OK`
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174002",
  "enrolledAt": "2023-12-01T11:00:00",
  "student": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "Alice Johnson"
  },
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Spring Boot Fundamentals"
  }
}
```

## 🚨 Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Enrollment not found with id: 789e1234-e89b-12d3-a456-426614174002",
  "path": "/api/v1/enrollments/789e1234-e89b-12d3-a456-426614174002",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 400 Validation Error
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/enrollments",
  "timestamp": "2023-12-01T10:30:00",
  "validationErrors": [
    {
      "field": "studentId",
      "rejectedValue": null,
      "message": "Student ID is required"
    },
    {
      "field": "courseId",
      "rejectedValue": null,
      "message": "Course ID is required"
    }
  ]
}
```

### 409 Conflict (Already Enrolled)
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Student is already enrolled in this course",
  "path": "/api/v1/enrollments",
  "timestamp": "2023-12-01T10:30:00",
  "details": {
    "studentId": "123e4567-e89b-12d3-a456-426614174000",
    "courseId": "456e7890-e89b-12d3-a456-426614174001",
    "existingEnrollmentId": "789e1234-e89b-12d3-a456-426614174002"
  }
}
```

## 🔧 cURL Examples

### Create Enrollment
```bash
curl -X POST http://localhost:8080/api/v1/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "123e4567-e89b-12d3-a456-426614174000",
    "courseId": "456e7890-e89b-12d3-a456-426614174001"
  }'
```

### Get Student's Enrollments
```bash
curl -X GET "http://localhost:8080/api/v1/enrollments/student/123e4567-e89b-12d3-a456-426614174000?page=0&size=10" \
  -H "Accept: application/json"
```

### Get Course Enrollments
```bash
curl -X GET "http://localhost:8080/api/v1/enrollments/course/456e7890-e89b-12d3-a456-426614174001" \
  -H "Accept: application/json"
```

### Bulk Enroll Student
```bash
curl -X POST http://localhost:8080/api/v1/enrollments/bulk/student/123e4567-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -d '{
    "courseIds": [
      "456e7890-e89b-12d3-a456-426614174001",
      "abc1234e-e89b-12d3-a456-426614174003"
    ]
  }'
```

### Check if Student is Enrolled
```bash
curl -X GET "http://localhost:8080/api/v1/enrollments/exists?studentId=123e4567-e89b-12d3-a456-426614174000&courseId=456e7890-e89b-12d3-a456-426614174001" \
  -H "Accept: application/json"
```

### Get Enrollment Statistics
```bash
curl -X GET http://localhost:8080/api/v1/enrollments/stats \
  -H "Accept: application/json"
```

---

## 📚 Related Documentation

- **[Students API](./students.md)** - Student management operations
- **[Courses API](./courses.md)** - Course management operations
- **[Reviews API](./reviews.md)** - Course review system
- **[Getting Started Guide](../guides/getting-started.md)** - Setup and configuration
- **[Database Schema](../reference/database-schema.md)** - Entity relationships

---

*For interactive testing, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)*
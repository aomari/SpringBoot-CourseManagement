# Enhanced Review API Examples

This document provides examples of the enhanced Review API with student relationships.

## Overview

The Review API has been enhanced to include student relationships:
- Each review now belongs to both a **Course** and a **Student**
- Students can write multiple reviews for the same course
- All existing endpoints have been updated to include student information
- New endpoints have been added for student-specific operations

## Database Schema Changes

### Migration Applied: V2__add_student_id_to_reviews.sql

```sql
-- Add student_id column to reviews table
ALTER TABLE reviews ADD COLUMN student_id UUID;

-- Add foreign key constraint
ALTER TABLE reviews ADD CONSTRAINT fk_reviews_student 
FOREIGN KEY (student_id) REFERENCES student(id);

-- Create indexes for better performance
CREATE INDEX idx_reviews_student_id ON reviews(student_id);
CREATE INDEX idx_reviews_course_student ON reviews(course_id, student_id);
```

## API Examples

### 1. Create Review for Course (Enhanced)

**POST** `/api/v1/courses/{courseId}/reviews`

**Request Body:**
```json
{
  "comment": "This course was excellent! Learned a lot about Spring Boot and microservices architecture. The instructor explained complex concepts very clearly.",
  "studentId": "987fcdeb-51a2-43d1-9b12-345678901234"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "comment": "This course was excellent! Learned a lot about Spring Boot and microservices architecture. The instructor explained complex concepts very clearly.",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "course": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "title": "Advanced Spring Boot Development",
    "instructorName": "Dr. John Smith"
  },
  "student": {
    "id": "987fcdeb-51a2-43d1-9b12-345678901234",
    "fullName": "Jane Doe",
    "email": "jane.doe@example.com"
  }
}
```

### 2. Get All Reviews (Enhanced Response)

**GET** `/api/v1/reviews`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "comment": "Great course! Very practical examples.",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "course": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Advanced Spring Boot Development",
      "instructorName": "Dr. John Smith"
    },
    "student": {
      "id": "987fcdeb-51a2-43d1-9b12-345678901234",
      "fullName": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  },
  {
    "id": "660f9511-f39c-52e5-b827-557766551111",
    "comment": "Challenging but rewarding course.",
    "createdAt": "2023-12-01T14:15:00",
    "updatedAt": "2023-12-01T14:15:00",
    "course": {
      "id": "234f5678-f90c-23e4-b567-537725285111",
      "title": "React Frontend Development",
      "instructorName": "Sarah Johnson"
    },
    "student": {
      "id": "098gfed-62b3-54e2-c678-456789012345",
      "fullName": "Mike Wilson",
      "email": "mike.wilson@example.com"
    }
  }
]
```

### 3. Update Review (Enhanced)

**PUT** `/api/v1/reviews/{reviewId}`

**Request Body:**
```json
{
  "comment": "Updated review: This course exceeded my expectations! The hands-on projects were particularly valuable.",
  "studentId": "987fcdeb-51a2-43d1-9b12-345678901234"
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "comment": "Updated review: This course exceeded my expectations! The hands-on projects were particularly valuable.",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T16:45:00",
  "course": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "title": "Advanced Spring Boot Development",
    "instructorName": "Dr. John Smith"
  },
  "student": {
    "id": "987fcdeb-51a2-43d1-9b12-345678901234",
    "fullName": "Jane Doe",
    "email": "jane.doe@example.com"
  }
}
```

## New Student-Related Endpoints

### 4. Get Reviews by Student

**GET** `/api/v1/students/{studentId}/reviews`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "comment": "Great course! Very practical examples.",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "course": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Advanced Spring Boot Development",
      "instructorName": "Dr. John Smith"
    },
    "student": {
      "id": "987fcdeb-51a2-43d1-9b12-345678901234",
      "fullName": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  }
]
```

### 5. Get Reviews by Course and Student

**GET** `/api/v1/courses/{courseId}/students/{studentId}/reviews`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "comment": "Great course! Very practical examples.",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "course": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Advanced Spring Boot Development",
      "instructorName": "Dr. John Smith"
    },
    "student": {
      "id": "987fcdeb-51a2-43d1-9b12-345678901234",
      "fullName": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  }
]
```

### 6. Count Reviews by Student

**GET** `/api/v1/students/{studentId}/reviews/count`

**Response (200 OK):**
```json
3
```

### 7. Search Reviews by Student Email

**GET** `/api/v1/reviews/search/student/email?email=jane.doe@example.com`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "comment": "Great course! Very practical examples.",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "course": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Advanced Spring Boot Development",
      "instructorName": "Dr. John Smith"
    },
    "student": {
      "id": "987fcdeb-51a2-43d1-9b12-345678901234",
      "fullName": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  }
]
```

### 8. Search Reviews by Student Name

**GET** `/api/v1/reviews/search/student/name?name=Jane`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "comment": "Great course! Very practical examples.",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "course": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "title": "Advanced Spring Boot Development",
      "instructorName": "Dr. John Smith"
    },
    "student": {
      "id": "987fcdeb-51a2-43d1-9b12-345678901234",
      "fullName": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  }
]
```

## Error Responses

### Student Not Found (404)
```json
{
  "timestamp": "2023-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 987fcdeb-51a2-43d1-9b12-345678901234",
  "path": "/api/v1/courses/123e4567-e89b-12d3-a456-426614174000/reviews"
}
```

### Validation Error (400)
```json
{
  "timestamp": "2023-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "studentId": "Student ID is required",
    "comment": "Comment is required"
  },
  "path": "/api/v1/courses/123e4567-e89b-12d3-a456-426614174000/reviews"
}
```

## Key Changes Summary

1. **ReviewRequest DTO**: Added `studentId` field with validation
2. **ReviewResponse DTO**: Added `StudentInfo` nested class with student details
3. **Review Entity**: Added `@ManyToOne` relationship with Student
4. **Repository**: Added methods for student-based queries
5. **Service**: Enhanced validation and new methods for student operations
6. **Controller**: Added new endpoints for student-specific operations
7. **Database**: Migration script to add `student_id` column and constraints

## Migration Notes

- Existing reviews will have `student_id` as NULL after migration
- In production, you would need to handle existing data appropriately
- Consider adding NOT NULL constraint after data migration
- The migration includes performance indexes for common query patterns
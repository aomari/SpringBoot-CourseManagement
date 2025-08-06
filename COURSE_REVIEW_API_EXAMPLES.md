# Course and Review API Examples

This document provides example JSON requests and responses for the Course and Review APIs.

## Course API Examples

### Base URL
```
http://localhost:8080/api/v1/courses
```

### 1. CREATE Course
**POST /api/v1/courses**

Request Body:
```json
{
  "title": "Introduction to Spring Boot",
  "instructorId": "123e4567-e89b-12d3-a456-426614174000"
}
```

Response (201 Created):
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Introduction to Spring Boot",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com"
  },
  "reviews": null
}
```

### 2. GET Course by ID (with reviews)
**GET /api/v1/courses/{id}**

Response (200 OK):
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Introduction to Spring Boot",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com"
  },
  "reviews": [
    {
      "id": "789e0123-e89b-12d3-a456-426614174002",
      "comment": "Excellent course! Learned a lot about Spring Boot fundamentals.",
      "createdAt": "2023-12-02T14:20:00",
      "updatedAt": "2023-12-02T14:20:00",
      "course": {
        "id": "456e7890-e89b-12d3-a456-426614174001",
        "title": "Introduction to Spring Boot",
        "instructorName": "John Doe"
      }
    }
  ]
}
```

### 3. GET All Courses
**GET /api/v1/courses**

Response (200 OK):
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Introduction to Spring Boot",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "instructor": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "fullName": "John Doe",
      "email": "john.doe@example.com"
    },
    "reviews": null
  },
  {
    "id": "567e8901-e89b-12d3-a456-426614174003",
    "title": "Advanced Java Programming",
    "createdAt": "2023-12-01T11:00:00",
    "updatedAt": "2023-12-01T11:00:00",
    "instructor": {
      "id": "234e5678-e89b-12d3-a456-426614174004",
      "fullName": "Jane Smith",
      "email": "jane.smith@example.com"
    },
    "reviews": null
  }
]
```

### 4. UPDATE Course
**PUT /api/v1/courses/{id}**

Request Body:
```json
{
  "title": "Advanced Spring Boot Development",
  "instructorId": "123e4567-e89b-12d3-a456-426614174000"
}
```

Response (200 OK):
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "title": "Advanced Spring Boot Development",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-03T09:15:00",
  "instructor": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com"
  },
  "reviews": null
}
```

### 5. DELETE Course
**DELETE /api/v1/courses/{id}**

Response: 204 No Content

### 6. Search Courses by Title
**GET /api/v1/courses/search/title?title=Spring**

Response (200 OK):
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Introduction to Spring Boot",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "instructor": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "fullName": "John Doe",
      "email": "john.doe@example.com"
    },
    "reviews": null
  }
]
```

### 7. Get Courses by Instructor
**GET /api/v1/courses/instructor/{instructorId}**

Response (200 OK): Similar to GET All Courses but filtered by instructor.

---

## Review API Examples

### Base URLs
```
http://localhost:8080/api/v1/courses/{courseId}/reviews  (nested under courses)
http://localhost:8080/api/v1/reviews                     (direct review operations)
```

### 1. ADD Review to Course
**POST /api/v1/courses/{courseId}/reviews**

Request Body:
```json
{
  "comment": "This course was excellent! I learned a lot about Spring Boot and best practices.",
  "studentId": "987fcdeb-51a2-43d1-9b12-345678901234"
}
```

Response (201 Created):
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174002",
  "comment": "This course was excellent! I learned a lot about Spring Boot and best practices.",
  "createdAt": "2023-12-02T14:20:00",
  "updatedAt": "2023-12-02T14:20:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Introduction to Spring Boot",
    "instructorName": "John Doe"
  }
}
```

### 2. GET Reviews for Course
**GET /api/v1/courses/{courseId}/reviews**

Response (200 OK):
```json
[
  {
    "id": "789e0123-e89b-12d3-a456-426614174002",
    "comment": "This course was excellent! I learned a lot about Spring Boot and best practices.",
    "createdAt": "2023-12-02T14:20:00",
    "updatedAt": "2023-12-02T14:20:00",
    "course": {
      "id": "456e7890-e89b-12d3-a456-426614174001",
      "title": "Introduction to Spring Boot",
      "instructorName": "John Doe"
    }
  },
  {
    "id": "890e1234-e89b-12d3-a456-426614174005",
    "comment": "Great content, but could use more practical examples.",
    "createdAt": "2023-12-01T16:45:00",
    "updatedAt": "2023-12-01T16:45:00",
    "course": {
      "id": "456e7890-e89b-12d3-a456-426614174001",
      "title": "Introduction to Spring Boot",
      "instructorName": "John Doe"
    }
  }
]
```

### 3. GET Review by ID
**GET /api/v1/reviews/{id}**

Response (200 OK):
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174002",
  "comment": "This course was excellent! I learned a lot about Spring Boot and best practices.",
  "createdAt": "2023-12-02T14:20:00",
  "updatedAt": "2023-12-02T14:20:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Introduction to Spring Boot",
    "instructorName": "John Doe"
  }
}
```

### 4. UPDATE Review
**PUT /api/v1/reviews/{id}**

Request Body:
```json
{
  "comment": "This course was outstanding! I learned a lot about Spring Boot, JPA, and microservices architecture.",
  "studentId": "987fcdeb-51a2-43d1-9b12-345678901234"
}
```

Response (200 OK):
```json
{
  "id": "789e0123-e89b-12d3-a456-426614174002",
  "comment": "This course was outstanding! I learned a lot about Spring Boot, JPA, and microservices architecture.",
  "createdAt": "2023-12-02T14:20:00",
  "updatedAt": "2023-12-03T10:30:00",
  "course": {
    "id": "456e7890-e89b-12d3-a456-426614174001",
    "title": "Introduction to Spring Boot",
    "instructorName": "John Doe"
  }
}
```

### 5. DELETE Review
**DELETE /api/v1/reviews/{id}**

Response: 204 No Content

### 6. GET All Reviews
**GET /api/v1/reviews**

Response (200 OK): Array of review objects similar to individual review response.

### 7. Search Reviews by Comment
**GET /api/v1/reviews/search/comment?keyword=excellent**

Response (200 OK): Array of reviews containing the keyword "excellent" in their comments.

### 8. Get Reviews by Instructor
**GET /api/v1/reviews/instructor/{instructorId}**

Response (200 OK): Array of all reviews for courses taught by the specified instructor.

### 9. Get Latest Reviews
**GET /api/v1/reviews/latest**

Response (200 OK): Array of reviews ordered by creation date (newest first).

### 10. Count Reviews for Course
**GET /api/v1/courses/{courseId}/reviews/count**

Response (200 OK):
```json
3
```

---

## Error Responses

### 400 Bad Request (Validation Error)
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/courses",
  "timestamp": "2023-12-01T10:30:00",
  "validationErrors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "Title is required"
    },
    {
      "field": "title",
      "rejectedValue": "ab",
      "message": "Title must be between 3 and 255 characters"
    }
  ]
}
```

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

### 409 Conflict
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Course already exists with title: Introduction to Spring Boot for instructor John Doe",
  "path": "/api/courses",
  "timestamp": "2023-12-01T10:30:00"
}
```

---

## Complete Endpoint List

### Course Endpoints
- **POST** `/api/courses` - Create course
- **GET** `/api/courses` - Get all courses
- **GET** `/api/courses/{id}` - Get course by ID (with reviews)
- **PUT** `/api/courses/{id}` - Update course
- **DELETE** `/api/courses/{id}` - Delete course
- **GET** `/api/courses/instructor/{instructorId}` - Get courses by instructor
- **GET** `/api/courses/search/title?title={title}` - Search courses by title
- **GET** `/api/courses/search/instructor?name={name}` - Search courses by instructor name
- **GET** `/api/courses/with-reviews` - Get all courses with reviews
- **GET** `/api/courses/instructor/{instructorId}/with-reviews` - Get instructor courses with reviews
- **GET** `/api/courses/{id}/exists` - Check if course exists
- **GET** `/api/courses/instructor/{instructorId}/count` - Count courses by instructor

### Review Endpoints
- **POST** `/api/courses/{courseId}/reviews` - Add review to course
- **GET** `/api/courses/{courseId}/reviews` - Get reviews for course
- **GET** `/api/reviews/{id}` - Get review by ID
- **PUT** `/api/reviews/{id}` - Update review
- **DELETE** `/api/reviews/{id}` - Delete review
- **GET** `/api/reviews` - Get all reviews
- **GET** `/api/reviews/search/comment?keyword={keyword}` - Search reviews by comment
- **GET** `/api/reviews/search/course?title={title}` - Search reviews by course title
- **GET** `/api/reviews/instructor/{instructorId}` - Get reviews by instructor
- **GET** `/api/reviews/latest` - Get latest reviews
- **GET** `/api/reviews/{id}/exists` - Check if review exists
- **GET** `/api/courses/{courseId}/reviews/count` - Count reviews for course
# Course Management API - Request Examples

This document provides comprehensive examples of API requests for the Course Management System.

## Base URL
```
http://localhost:8080/api/v1
```

## Swagger UI
Visit: `http://localhost:8080/swagger-ui.html` for interactive API documentation.

---

## Instructor Details API

### 1. Create Instructor Details

**POST** `/instructor-details`

```json
{
  "youtubeChannel": "https://youtube.com/@johndoe",
  "hobby": "Playing guitar and coding open source projects"
}
```

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "youtubeChannel": "https://youtube.com/@johndoe",
  "hobby": "Playing guitar and coding open source projects",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00"
}
```

### 2. Get Instructor Details by ID

**GET** `/instructor-details/{id}`

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "youtubeChannel": "https://youtube.com/@johndoe",
  "hobby": "Playing guitar and coding open source projects",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00"
}
```

### 3. Update Instructor Details

**PUT** `/instructor-details/{id}`

```json
{
  "youtubeChannel": "https://youtube.com/@johndoe-updated",
  "hobby": "Playing piano and teaching programming"
}
```

### 4. Search by YouTube Channel

**GET** `/instructor-details/search/youtube?channel=johndoe`

### 5. Search by Hobby

**GET** `/instructor-details/search/hobby?hobby=guitar`

### 6. Get Orphaned Details

**GET** `/instructor-details/orphaned`

---

## Instructor API

### 1. Create Instructor (without details)

**POST** `/instructors`

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
  "id": "456e7890-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructorDetails": null
}
```

### 2. Create Instructor (with embedded details)

**POST** `/instructors`

```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "instructorDetails": {
    "youtubeChannel": "https://youtube.com/@janesmith",
    "hobby": "Photography and web development"
  }
}
```

**Response (201 Created):**
```json
{
  "id": "789e1234-e89b-12d3-a456-426614174000",
  "firstName": "Jane",
  "lastName": "Smith",
  "fullName": "Jane Smith",
  "email": "jane.smith@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00",
  "instructorDetails": {
    "id": "abc1234e-e89b-12d3-a456-426614174000",
    "youtubeChannel": "https://youtube.com/@janesmith",
    "hobby": "Photography and web development",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00"
  }
}
```

### 3. Get Instructor by ID

**GET** `/instructors/{id}`

### 4. Get All Instructors

**GET** `/instructors`

**Response (200 OK):**
```json
[
  {
    "id": "456e7890-e89b-12d3-a456-426614174000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "instructorDetails": null
  },
  {
    "id": "789e1234-e89b-12d3-a456-426614174000",
    "firstName": "Jane",
    "lastName": "Smith",
    "fullName": "Jane Smith",
    "email": "jane.smith@example.com",
    "createdAt": "2023-12-01T10:30:00",
    "updatedAt": "2023-12-01T10:30:00",
    "instructorDetails": {
      "id": "abc1234e-e89b-12d3-a456-426614174000",
      "youtubeChannel": "https://youtube.com/@janesmith",
      "hobby": "Photography and web development",
      "createdAt": "2023-12-01T10:30:00",
      "updatedAt": "2023-12-01T10:30:00"
    }
  }
]
```

### 5. Update Instructor

**PUT** `/instructors/{id}`

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe.updated@example.com",
  "instructorDetails": {
    "youtubeChannel": "https://youtube.com/@johndoe-new",
    "hobby": "Teaching and mentoring"
  }
}
```

### 6. Get Instructor by Email

**GET** `/instructors/email/john.doe@example.com`

### 7. Search Instructors by Name

**GET** `/instructors/search?name=John`

### 8. Get Instructors with Details

**GET** `/instructors/with-details`

### 9. Get Instructors without Details

**GET** `/instructors/without-details`

### 10. Add Existing Details to Instructor

**PUT** `/instructors/{instructorId}/details/{detailsId}`

### 11. Remove Details from Instructor

**DELETE** `/instructors/{instructorId}/details`

### 12. Check if Instructor Exists by ID

**GET** `/instructors/{id}/exists`

**Response:**
```json
true
```

### 13. Check if Instructor Exists by Email

**GET** `/instructors/email/john.doe@example.com/exists`

**Response:**
```json
false
```

---

## Error Responses

### 404 Not Found
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Instructor not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/instructors/123e4567-e89b-12d3-a456-426614174000",
  "timestamp": "2023-12-01T10:30:00"
}
```

### 400 Validation Error
```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/instructors",
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

### 409 Already Exists
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Instructor already exists with email: john.doe@example.com",
  "path": "/api/v1/instructors",
  "timestamp": "2023-12-01T10:30:00"
}
```

---

## cURL Examples

### Create Instructor with Details
```bash
curl -X POST http://localhost:8080/api/v1/instructors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.johnson@example.com",
    "instructorDetails": {
      "youtubeChannel": "https://youtube.com/@alicejohnson",
      "hobby": "Machine learning and data science"
    }
  }'
```

### Get All Instructors
```bash
curl -X GET http://localhost:8080/api/v1/instructors \
  -H "Accept: application/json"
```

### Update Instructor
```bash
curl -X PUT http://localhost:8080/api/v1/instructors/456e7890-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "j.doe@example.com"
  }'
```

### Delete Instructor
```bash
curl -X DELETE http://localhost:8080/api/v1/instructors/456e7890-e89b-12d3-a456-426614174000
```

---

## Testing with Postman

1. Import the OpenAPI specification from: `http://localhost:8080/api-docs`
2. Set base URL: `http://localhost:8080/api/v1`
3. Use the provided JSON examples for request bodies
4. Set `Content-Type: application/json` for POST/PUT requests

---

## Pagination (Future Enhancement)

For large datasets, consider adding pagination parameters:

```
GET /instructors?page=0&size=10&sort=lastName,asc
```

This structure provides room for future enhancements while maintaining the current simple API design.
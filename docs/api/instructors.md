# 👨‍🏫 Instructors API Reference

Complete API reference for managing instructors and their detailed profiles in the Course Management System.

## Overview

The Instructors API provides endpoints for managing instructor profiles and their associated details. Instructors have a one-to-one relationship with InstructorDetails, allowing for rich profile information.

### Base URL
```
http://localhost:8080/api/v1
```

### Related Endpoints
- **Instructors**: `/instructors/*`
- **Instructor Details**: `/instructor-details/*`

## 👨‍🏫 Instructor Management

### Create Instructor

Create a new instructor with optional details.

**`POST /instructors`**

#### Request Body Options

**Option 1: Basic Instructor**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

**Option 2: Instructor with Details**
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

#### Response `201 Created`
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

### Get All Instructors

Retrieve all instructors in the system.

**`GET /instructors`**

#### Response `200 OK`
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
      "hobby": "Photography and web development"
    }
  }
]
```

### Get Instructor by ID

Retrieve a specific instructor by their ID.

**`GET /instructors/{id}`**

#### Path Parameters
- `id` (UUID) - The instructor's unique identifier

#### Response `200 OK`
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
    "hobby": "Photography and web development"
  }
}
```

### Update Instructor

Update an existing instructor's information.

**`PUT /instructors/{id}`**

#### Path Parameters
- `id` (UUID) - The instructor's unique identifier

#### Request Body
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "instructorDetails": {
    "youtubeChannel": "https://youtube.com/@johnsmith-updated",
    "hobby": "Teaching and mentoring developers"
  }
}
```

#### Response `200 OK`
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174000",
  "firstName": "John",
  "lastName": "Smith",
  "fullName": "John Smith",
  "email": "john.smith@example.com",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T11:45:00",
  "instructorDetails": {
    "id": "def5678f-e89b-12d3-a456-426614174000",
    "youtubeChannel": "https://youtube.com/@johnsmith-updated",
    "hobby": "Teaching and mentoring developers",
    "updatedAt": "2023-12-01T11:45:00"
  }
}
```

### Delete Instructor

Delete an instructor and their associated details.

**`DELETE /instructors/{id}`**

#### Path Parameters
- `id` (UUID) - The instructor's unique identifier

#### Response `200 OK`
```json
{
  "message": "Instructor deleted successfully",
  "id": "456e7890-e89b-12d3-a456-426614174000",
  "deletedAt": "2023-12-01T12:00:00"
}
```

## 🔍 Search & Filter Operations

### Search by Name

Find instructors by first name, last name, or full name.

**`GET /instructors/search?name={name}`**

#### Query Parameters
- `name` (string) - Search term for instructor names

#### Example
```bash
GET /instructors/search?name=John
```

### Get by Email

Find an instructor by their email address.

**`GET /instructors/email/{email}`**

#### Path Parameters
- `email` (string) - The instructor's email address

#### Example
```bash
GET /instructors/email/john.doe@example.com
```

### Get Instructors with Details

Retrieve only instructors who have associated details.

**`GET /instructors/with-details`**

### Get Instructors without Details

Retrieve only instructors who don't have associated details.

**`GET /instructors/without-details`**

## 🔗 Relationship Management

### Add Details to Instructor

Associate existing instructor details with an instructor.

**`PUT /instructors/{instructorId}/details/{detailsId}`**

#### Path Parameters
- `instructorId` (UUID) - The instructor's unique identifier
- `detailsId` (UUID) - The instructor details unique identifier

### Remove Details from Instructor

Remove the association between an instructor and their details.

**`DELETE /instructors/{instructorId}/details`**

#### Path Parameters
- `instructorId` (UUID) - The instructor's unique identifier

## 📝 Instructor Details Management

### Create Instructor Details

Create standalone instructor details (can be associated later).

**`POST /instructor-details`**

#### Request Body
```json
{
  "youtubeChannel": "https://youtube.com/@johndoe",
  "hobby": "Playing guitar and coding open source projects"
}
```

#### Response `201 Created`
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "youtubeChannel": "https://youtube.com/@johndoe",
  "hobby": "Playing guitar and coding open source projects",
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T10:30:00"
}
```

### Get All Instructor Details

**`GET /instructor-details`**

### Get Details by ID

**`GET /instructor-details/{id}`**

### Update Instructor Details

**`PUT /instructor-details/{id}`**

#### Request Body
```json
{
  "youtubeChannel": "https://youtube.com/@johndoe-updated",
  "hobby": "Playing piano and teaching programming"
}
```

### Delete Instructor Details

**`DELETE /instructor-details/{id}`**

### Search Details by YouTube Channel

**`GET /instructor-details/search/youtube?channel={channel}`**

#### Query Parameters
- `channel` (string) - YouTube channel name or URL fragment

### Search Details by Hobby

**`GET /instructor-details/search/hobby?hobby={hobby}`**

#### Query Parameters
- `hobby` (string) - Hobby keyword

### Get Orphaned Details

Retrieve instructor details that aren't associated with any instructor.

**`GET /instructor-details/orphaned`**

## ✅ Validation & Existence Checks

### Check if Instructor Exists by ID

**`GET /instructors/{id}/exists`**

#### Response `200 OK`
```json
true
```

### Check if Instructor Exists by Email

**`GET /instructors/email/{email}/exists`**

#### Response `200 OK`
```json
false
```

## 🚨 Error Responses

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

### 409 Conflict (Already Exists)
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Instructor already exists with email: john.doe@example.com",
  "path": "/api/v1/instructors",
  "timestamp": "2023-12-01T10:30:00"
}
```

## 🔧 cURL Examples

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

### Search by Name
```bash
curl -X GET "http://localhost:8080/api/v1/instructors/search?name=John" \
  -H "Accept: application/json"
```

### Delete Instructor
```bash
curl -X DELETE http://localhost:8080/api/v1/instructors/456e7890-e89b-12d3-a456-426614174000
```

---

## 📚 Related Documentation

- **[Students API](./students.md)** - Student management operations
- **[Courses API](./courses.md)** - Course catalog management
- **[Reviews API](./reviews.md)** - Course review system
- **[Getting Started Guide](../guides/getting-started.md)** - Setup and configuration
- **[Database Schema](../reference/database-schema.md)** - Entity relationships

---

*For interactive testing, visit the [Swagger UI](http://localhost:8080/swagger-ui.html)*
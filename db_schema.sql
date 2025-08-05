-- Enable the pgcrypto extension (provides gen_random_uuid())
-- This is preferred over uuid-ossp in PostgreSQL 13+
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. instructor_details table
CREATE TABLE instructor_details (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    youtube_channel VARCHAR(255) NOT NULL,
    hoppy TEXT
);

-- 2. instructor table
CREATE TABLE instructor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    instructor_details_id UUID UNIQUE,
    FOREIGN KEY (instructor_details_id) REFERENCES instructor_details(id) ON DELETE SET NULL
);

-- 3. course table
CREATE TABLE course (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    instructor_id UUID NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES instructor(id) ON DELETE CASCADE
);

-- 4. student table
CREATE TABLE student (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- 5. reviews table
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comment TEXT NOT NULL,
    course_id UUID NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- 6. course_student junction table (many-to-many)
CREATE TABLE course_student (
    course_id UUID NOT NULL,
    student_id UUID NOT NULL,
    PRIMARY KEY (course_id, student_id),
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);
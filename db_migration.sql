-- Migration script to add missing timestamp columns
-- Run this against your PostgreSQL database

-- Add timestamp columns to instructor table
ALTER TABLE instructor 
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add timestamp columns to instructor_details table  
ALTER TABLE instructor_details 
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Update existing records to have current timestamp (optional)
UPDATE instructor SET created_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE instructor_details SET created_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
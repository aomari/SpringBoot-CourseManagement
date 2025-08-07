-- Migration script to add student_id to reviews table
-- Version: V2
-- Description: Add student_id column and foreign key constraint to reviews table if they don't exist

-- Add student_id column to reviews table if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'reviews' 
        AND column_name = 'student_id'
    ) THEN
        ALTER TABLE reviews ADD COLUMN student_id UUID;
    END IF;
END $$;

-- Add foreign key constraint if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE table_name = 'reviews' 
        AND constraint_name = 'fk_reviews_student'
    ) THEN
        ALTER TABLE reviews 
        ADD CONSTRAINT fk_reviews_student 
        FOREIGN KEY (student_id) REFERENCES student(id);
    END IF;
END $$;

-- Create index for better query performance if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE tablename = 'reviews' 
        AND indexname = 'idx_reviews_student_id'
    ) THEN
        CREATE INDEX idx_reviews_student_id ON reviews(student_id);
    END IF;
END $$;

-- Create composite index for course and student queries if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE tablename = 'reviews' 
        AND indexname = 'idx_reviews_course_student'
    ) THEN
        CREATE INDEX idx_reviews_course_student ON reviews(course_id, student_id);
    END IF;
END $$;

-- Update existing reviews to have a student_id (for demo purposes)
-- In a real scenario, you might need to handle this differently based on your data
-- For now, we'll leave student_id as NULL for existing reviews
-- You could also create a default student or handle this in application logic

-- Add NOT NULL constraint after handling existing data
-- For this demo, we'll keep it nullable to handle existing reviews
-- ALTER TABLE reviews ALTER COLUMN student_id SET NOT NULL;

-- Note: In production, you would want to:
-- 1. Handle existing review data appropriately
-- 2. Consider adding the NOT NULL constraint after data migration
-- 3. Possibly create a migration strategy for existing reviews
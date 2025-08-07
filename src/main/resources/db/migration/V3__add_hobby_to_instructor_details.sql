-- Migration script to add hobby column to instructor_details table
-- Version: V3
-- Description: Add hobby column to instructor_details table and clean up old hoppy column

-- First, drop the old hoppy column if it exists
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'instructor_details' 
        AND column_name = 'hoppy'
    ) THEN
        ALTER TABLE instructor_details DROP COLUMN hoppy;
    END IF;
END $$;

-- Add hobby column to instructor_details table if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'instructor_details' 
        AND column_name = 'hobby'
    ) THEN
        ALTER TABLE instructor_details ADD COLUMN hobby TEXT;
    END IF;
END $$; 
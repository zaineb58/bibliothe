-- Quick setup script for signalements system
ALTER SESSION SET CURRENT_SCHEMA = bibliothe;

-- Add signalements column (will fail silently if already exists)
ALTER TABLE adherent ADD signalements NUMBER DEFAULT 0 NOT NULL;

-- Show table structure
DESCRIBE adherent;

-- Add signalements column to adherent table
-- Connect to the bibliothe schema before running this script

-- Add the signalements column with default value 0
ALTER TABLE adherent 
ADD signalements INT DEFAULT 0 NOT NULL;

-- Verify the change
DESCRIBE adherent;

SELECT 'Signalements column added successfully!' AS Message;

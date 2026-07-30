ALTER TABLE meeting_analyses
ADD COLUMN risks JSONB NOT NULL;

ALTER TABLE meeting_analyses
ADD COLUMN next_steps JSONB NOT NULL;


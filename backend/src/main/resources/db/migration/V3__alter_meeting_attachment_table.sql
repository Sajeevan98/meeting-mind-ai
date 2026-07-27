ALTER TABLE meeting_attachments
ADD COLUMN uuid UUID NOT NULL;

ALTER TABLE meeting_attachments
ADD CONSTRAINT uk_meeting_attachments_uuid UNIQUE (uuid);

CREATE INDEX idx_meeting_attachment_uuid
ON meeting_attachments(uuid);
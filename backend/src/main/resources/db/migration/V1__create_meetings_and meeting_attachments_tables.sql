CREATE TABLE IF NOT EXISTS meetings (

    id BIGSERIAL PRIMARY KEY,

    uuid UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_meetings_uuid UNIQUE (uuid),

    CHECK ( status IN ( 'UPLOADED', 'PROCESSING', 'COMPLETED', 'FAILED' ) )
);

CREATE TABLE IF NOT EXISTS meeting_attachments (

    id BIGSERIAL PRIMARY KEY,

    meeting_id BIGINT NOT NULL,

    original_file_name VARCHAR(255) NOT NULL,

    stored_file_name VARCHAR(255) NOT NULL,

    file_path VARCHAR(500) NOT NULL,

    file_type VARCHAR(255) NOT NULL,

    file_extension VARCHAR(20) NOT NULL,

    file_size BIGINT NOT NULL,

    checksum VARCHAR(64) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_meeting_attachments
        FOREIGN KEY (meeting_id)
        REFERENCES meetings(id)
        ON DELETE CASCADE
        -- It is belong to the meeting. Database should automatically delete them.
        -- This matches JPA: orphanRemoval = true, cascade = CascadeType.ALL
);

--CREATE INDEX idx_meeting_uuid
--ON meetings(uuid);

CREATE INDEX idx_attachment_meeting
ON meeting_attachments(meeting_id);

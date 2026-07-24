CREATE TABLE IF NOT EXISTS meeting_analyses (

    id BIGSERIAL PRIMARY KEY,

    uuid UUID NOT NULL,

    meeting_id BIGINT NOT NULL,

    summary TEXT NOT NULL,

    action_items JSONB NOT NULL,

    decisions JSONB NOT NULL,

    raw_ai_response TEXT,

    provider VARCHAR(30) NOT NULL,

    model VARCHAR(100) NOT NULL,

    analysis_version INTEGER NOT NULL,

    prompt_version INTEGER NOT NULL,

    processing_time_ms BIGINT NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_meeting_analysis UNIQUE (uuid),

    CHECK ( provider IN ('OPENAI', 'GEMINI') ),

    CHECK ( status IN ('PROCESSING', 'COMPLETED', 'FAILED') ),

    CONSTRAINT fk_meeting_analyses
        FOREIGN KEY (meeting_id)
        REFERENCES meetings(id)
        ON DELETE CASCADE

);

CREATE INDEX idx_meeting_analyses_meeting_id
ON meeting_analyses(meeting_id);

-- for getting "latest analysis" of meeting
CREATE INDEX idx_meeting_analyses_id_version
ON meeting_analyses(meeting_id, analysis_version DESC);
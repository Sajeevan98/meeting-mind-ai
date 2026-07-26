package com.sajee.meeting_mind_ai.meeting.dto.response;

import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;

import java.time.Instant;
import java.util.UUID;

public record MeetingResponse(

        UUID uuid,

        String title,

        MeetingStatus status,

        int attachmentCount,

        int analysisCount,

        Instant createdAt,

        Instant updatedAt
) {
}

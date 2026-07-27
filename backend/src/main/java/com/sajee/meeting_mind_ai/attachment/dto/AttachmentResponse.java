package com.sajee.meeting_mind_ai.attachment.dto;

import java.time.Instant;
import java.util.UUID;

public record AttachmentResponse(

        UUID uuid,

        String originalFileName,

        String storedFileName,

        String fileType,

        String fileExtension,

        Long fileSize,

        Instant createdAt
) {
}

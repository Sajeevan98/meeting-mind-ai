package com.sajee.meeting_mind_ai.storage.dto;

public record StoredFile(

        String originalFileName,

        String storedFileName,

        String filePath,

        String fileType,

        String fileExtension,

        Long fileSize,

        String checksum
) {
}

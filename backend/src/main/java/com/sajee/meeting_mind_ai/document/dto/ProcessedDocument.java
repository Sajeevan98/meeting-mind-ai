package com.sajee.meeting_mind_ai.document.dto;

public record ProcessedDocument(

        String extractedText,

        int characterCount,

        int wordCount
) {
}

package com.sajee.meeting_mind_ai.analysis.dto;

import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.model.ActionItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MeetingAnalysisResponse(

        UUID uuid,

        String summary,

        List<ActionItem> actionItems,

        List<String> decisions,

        List<String> risks,

        List<String> nextSteps,

        AiProvider provider,

        String model,

        Integer analysisVersion,

        Integer promptVersion,

        Long processingTimeMs,

        AnalysisStatus status,

        Instant createdAt
) {
}

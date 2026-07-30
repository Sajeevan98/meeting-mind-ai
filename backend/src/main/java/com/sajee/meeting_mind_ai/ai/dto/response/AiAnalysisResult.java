package com.sajee.meeting_mind_ai.ai.dto.response;

import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.model.ActionItem;

import java.util.List;

public record AiAnalysisResult(

        String summary,

        List<ActionItem> actionItems,

        List<String> decisions,

        List<String> risks,

        List<String> nextSteps,

        Long processingTimeMs,

        AiProvider provider,

        String model,

        Integer promptVersion
) {
}

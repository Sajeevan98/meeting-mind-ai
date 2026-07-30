package com.sajee.meeting_mind_ai.ai.dto.response;

import com.sajee.meeting_mind_ai.analysis.model.ActionItem;

import java.util.List;

public record GeminiResponse(

        String summary,

        List<ActionItem> actionItems,

        List<String> decisions,

        List<String> risks,

        List<String> nextSteps
) {
}

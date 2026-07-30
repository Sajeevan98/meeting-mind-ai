package com.sajee.meeting_mind_ai.ai.dto.request;

import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AiAnalyzeRequest(

        @NotNull(message = "AI provider is required.")
        AiProvider aiProvider,

        @NotBlank(message = "Model is required.")
        String model
) {
}

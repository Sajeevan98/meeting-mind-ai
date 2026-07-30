package com.sajee.meeting_mind_ai.ai.strategy;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;

public interface AiModelStrategy {

    AiProvider provider();

    AiAnalysisResult analyze(ProcessedDocument document, AiAnalyzeRequest request);
}
package com.sajee.meeting_mind_ai.ai.service;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;

public interface AiService {

    AiAnalysisResult analyzeMeeting(ProcessedDocument document, AiAnalyzeRequest request);

}

package com.sajee.meeting_mind_ai.analysis.service;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;

import java.util.List;
import java.util.UUID;

public interface MeetingAnalysisService {

    MeetingAnalysisResponse analyze(UUID meetingUuid, AiAnalyzeRequest request);

    List<MeetingAnalysisResponse> getAnalyses(UUID meetingUuid);

    MeetingAnalysisResponse get(UUID analysisUuid);

    void delete(UUID analysisUuid);
}

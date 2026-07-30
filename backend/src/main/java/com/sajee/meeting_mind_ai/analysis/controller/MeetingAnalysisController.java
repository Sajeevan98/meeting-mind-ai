package com.sajee.meeting_mind_ai.analysis.controller;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.service.MeetingAnalysisService;
import com.sajee.meeting_mind_ai.common.response.ApiResponse;
import com.sajee.meeting_mind_ai.common.util.ApiEndpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiEndpoints.ANALYSES)
@RequiredArgsConstructor
public class MeetingAnalysisController {

    private final MeetingAnalysisService meetingAnalysisService;

    @PostMapping("/meeting/{meetingUuid}")
    public ApiResponse<MeetingAnalysisResponse> analyzeMeeting(
            @PathVariable UUID meetingUuid,
            @Valid @RequestBody AiAnalyzeRequest request) {

        MeetingAnalysisResponse response =
                meetingAnalysisService.analyze(meetingUuid, request);

        return ApiResponse.success("Meeting analyzed successfully.", response);
    }

    @GetMapping("/{analysisUuid}")
    public ApiResponse<MeetingAnalysisResponse> getByUuid(@PathVariable UUID analysisUuid) {

        return ApiResponse.success(
                "Analysis retrieved successfully.",
                meetingAnalysisService.get(analysisUuid)
        );
    }

    @GetMapping("/meeting/{meetingUuid}")
    public ApiResponse<List<MeetingAnalysisResponse>> getMeetingAnalyses(@PathVariable UUID meetingUuid) {

        return ApiResponse.success(
                "Meeting analyses retrieved successfully.",
                meetingAnalysisService.getAnalyses(meetingUuid)
        );
    }

    @DeleteMapping("/{analysisUuid}")
    public ApiResponse<Void> delete(@PathVariable UUID analysisUuid) {

        meetingAnalysisService.delete(analysisUuid);

        return ApiResponse.success("Meeting analysis deleted successfully.", null);
    }
}

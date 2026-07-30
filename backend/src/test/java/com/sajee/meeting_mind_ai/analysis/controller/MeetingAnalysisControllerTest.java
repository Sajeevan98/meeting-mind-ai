package com.sajee.meeting_mind_ai.analysis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.service.MeetingAnalysisService;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MeetingAnalysisController.class)
class MeetingAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private MeetingAnalysisService meetingAnalysisService;

    // Test Data
    private MeetingAnalysisResponse response() {

        return new MeetingAnalysisResponse(
                UUID.randomUUID(),
                "Summary",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                AiProvider.GEMINI,
                "gemini-3.5-flash",
                1,
                1,
                1000L,
                AnalysisStatus.COMPLETED,
                Instant.now()
        );
    }

    // Analyze Meeting
    @Test
    void shouldAnalyzeMeetingSuccessfully() throws Exception {

        UUID meetingUuid = UUID.randomUUID();

        AiAnalyzeRequest request =
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini-3.5-flash");

        when(meetingAnalysisService.analyze(meetingUuid, request))
                .thenReturn(response());

        mockMvc.perform(post("/api/v1/analyses/meeting/{meetingUuid}", meetingUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Meeting analyzed successfully."))
                .andExpect(jsonPath("$.data.provider")
                        .value("GEMINI"));
    }

    // Get Analysis History
    @Test
    void shouldReturnMeetingAnalyses() throws Exception {

        UUID meetingUuid = UUID.randomUUID();

        when(meetingAnalysisService.getAnalyses(meetingUuid))
                .thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/analyses/meeting/{meetingUuid}", meetingUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    // Get Analysis
    @Test
    void shouldReturnSingleAnalysis() throws Exception {

        UUID analysisUuid = UUID.randomUUID();

        when(meetingAnalysisService.get(analysisUuid))
                .thenReturn(response());

        mockMvc.perform(get("/api/v1/analyses/{analysisUuid}", analysisUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.model")
                        .value("gemini-3.5-flash"));
    }

    // Delete Analysis
    @Test
    void shouldDeleteAnalysis() throws Exception {

        UUID analysisUuid = UUID.randomUUID();

        doNothing()
                .when(meetingAnalysisService)
                .delete(analysisUuid);

        mockMvc.perform(delete("/api/v1/analyses/{analysisUuid}", analysisUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Meeting analysis deleted successfully."));

        verify(meetingAnalysisService).delete(analysisUuid);
    }

    // Meeting Not Found
    @Test
    void shouldReturn404WhenMeetingNotFound() throws Exception {

        UUID meetingUuid = UUID.randomUUID();

        AiAnalyzeRequest request =
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini-3.5-flash");

        when(meetingAnalysisService.analyze(meetingUuid, request))
                .thenThrow(new ResourceNotFoundException("Meeting not found"));

        mockMvc.perform(post("/api/v1/analyses/meeting/{meetingUuid}", meetingUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // Invalid Request
    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/api/v1/analyses/meeting/{meetingUuid}",
                        UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}
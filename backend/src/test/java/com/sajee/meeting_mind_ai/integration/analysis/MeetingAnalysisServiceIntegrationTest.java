package com.sajee.meeting_mind_ai.integration.analysis;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.service.AiService;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.model.ActionItem;
import com.sajee.meeting_mind_ai.analysis.repository.MeetingAnalysisRepository;
import com.sajee.meeting_mind_ai.analysis.service.MeetingAnalysisService;
import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import com.sajee.meeting_mind_ai.attachment.service.MeetingAttachmentService;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// run cmd: mvn -Dtest=MeetingAnalysisServiceIntegrationTest test

@SpringBootTest
@ActiveProfiles("test")
public class MeetingAnalysisServiceIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MeetingAnalysisService analysisService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingAnalysisRepository analysisRepository;

    @MockitoBean
    private AiService aiService;

    @Autowired
    private MeetingAttachmentService attachmentService;

    @Test
    void shouldCreateAnalysisSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        createAttachment(meeting.uuid());

        AiAnalysisResult fakeResult = new AiAnalysisResult(
                "This is a test summary.",
                List.of(new ActionItem("John", "Finish API optimization", "Monday")),
                List.of("Deployment will happen on Wednesday."),
                List.of("API performance risk."),
                List.of("Continue development."),
                100L,
                AiProvider.GEMINI, "gemini-test-model",
                1
        );

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(fakeResult);

        AiAnalyzeRequest request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-test-model"
        );

        // Act
        MeetingAnalysisResponse response = analysisService.analyze(
                meeting.uuid(),
                request
        );

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.uuid()).isNotNull();

        assertThat(response.summary())
                .isEqualTo("This is a test summary.");

        assertThat(response.provider())
                .isEqualTo(AiProvider.GEMINI);

        assertThat(response.model())
                .isEqualTo("gemini-test-model");

        assertThat(response.analysisVersion())
                .isEqualTo(1);

        assertThat(response.status())
                .isEqualTo(AnalysisStatus.COMPLETED);

        assertThat(analysisRepository.findByUuid(response.uuid())).isPresent();
    }

    @Test
    void shouldCreateNextAnalysisVersionSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        createAttachment(meeting.uuid());

        AiAnalysisResult fakeResult = new AiAnalysisResult(
                "Test summary",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                100L,
                AiProvider.GEMINI,
                "gemini-test-model",
                1
        );

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(fakeResult);

        AiAnalyzeRequest request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-test-model"
        );

        // Act
        MeetingAnalysisResponse first = analysisService.analyze(
                meeting.uuid(),
                request
        );

        MeetingAnalysisResponse second = analysisService.analyze(
                meeting.uuid(),
                request
        );

        // Assert
        assertThat(first.analysisVersion())
                .isEqualTo(1);

        assertThat(second.analysisVersion())
                .isEqualTo(2);
    }

    // All analyses using meeting-uuid
    @Test
    void shouldGetMeetingAnalysesSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        createAttachment(meeting.uuid());

        AiAnalysisResult fakeResult = new AiAnalysisResult(
                "Test summary",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                100L,
                AiProvider.GEMINI,
                "gemini-test-model",
                1
        );

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(fakeResult);

        AiAnalyzeRequest request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-test-model"
        );

        analysisService.analyze(
                meeting.uuid(),
                request
        );

        // Act
        List<MeetingAnalysisResponse> analyses = analysisService.getAnalyses(meeting.uuid());

        // Assert
        assertThat(analyses).hasSize(1);

        assertThat(analyses.get(0).summary())
                .isEqualTo("Test summary");

        assertThat(analyses.get(0).analysisVersion())
                .isEqualTo(1);
    }

    // Single analysis using analysis-uuid
    @Test
    void shouldGetAnalysisSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        createAttachment(meeting.uuid());

        AiAnalysisResult fakeResult =
                new AiAnalysisResult(
                        "Single analysis summary",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        100L,
                        AiProvider.GEMINI,
                        "gemini-test-model",
                        1
                );

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(fakeResult);

        AiAnalyzeRequest request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-test-model"
        );

        MeetingAnalysisResponse created = analysisService.analyze(
                meeting.uuid(),
                request
        );

        // Act
        MeetingAnalysisResponse result = analysisService.get(created.uuid());

        // Assert
        assertThat(result).isNotNull();

        assertThat(result.uuid())
                .isEqualTo(created.uuid());

        assertThat(result.summary())
                .isEqualTo("Single analysis summary");
    }

    @Test
    void shouldDeleteAnalysisSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        createAttachment(meeting.uuid());

        AiAnalysisResult fakeResult = new AiAnalysisResult(
                "Analysis to delete",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                100L,
                AiProvider.GEMINI,
                "gemini-test-model",
                1
        );

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(fakeResult);

        AiAnalyzeRequest request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-test-model"
        );

        MeetingAnalysisResponse created = analysisService.analyze(
                meeting.uuid(),
                request
        );

        assertThat(analysisRepository.findByUuid(created.uuid())).isPresent();

        // Act
        analysisService.delete(created.uuid());

        // Assert
        assertThat(analysisRepository.findByUuid(created.uuid())).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenAnalysisNotFound() {

        // Arrange
        UUID analysisUuid = UUID.randomUUID();

        // Act + Assert
        assertThatThrownBy(() -> analysisService.get(analysisUuid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Analysis not found");
    }



    // ==== Helper methods ====
    private MeetingResponse createMeeting() {

        CreateMeetingRequest request = new CreateMeetingRequest(
                "Analysis Integration Test",
                "Testing meeting analysis."
        );
        return meetingService.create(request);
    }

    private AttachmentResponse createAttachment(UUID meetingUuid) {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "meeting.txt",
                "text/plain",
                "John will finish the API optimization by Monday.".getBytes()
        );
        return attachmentService.upload(meetingUuid, file);
    }
}

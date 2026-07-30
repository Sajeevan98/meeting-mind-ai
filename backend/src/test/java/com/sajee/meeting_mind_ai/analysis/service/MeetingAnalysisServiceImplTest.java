package com.sajee.meeting_mind_ai.analysis.controller;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.service.AiService;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.entity.MeetingAnalysis;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.mapper.MeetingAnalysisMapper;
import com.sajee.meeting_mind_ai.analysis.repository.MeetingAnalysisRepository;
import com.sajee.meeting_mind_ai.analysis.service.MeetingAnalysisServiceImpl;
import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.document.service.DocumentProcessingService;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingAnalysisServiceImplTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MeetingAnalysisRepository analysisRepository;

    @Mock
    private DocumentProcessingService documentProcessingService;

    @Mock
    private AiService aiService;

    @Mock
    private MeetingAnalysisMapper meetingAnalysisMapper;

    @InjectMocks
    private MeetingAnalysisServiceImpl meetingAnalysisService;

    // Meeting not found
    @Test
    void shouldThrowExceptionWhenMeetingNotFound() {

        UUID meetingUuid = UUID.randomUUID();

        AiAnalyzeRequest request =
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini-3.5-flash");

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                meetingAnalysisService.analyze(meetingUuid, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Meeting not found with UUID: " + meetingUuid);

        verify(meetingRepository).findByUuid(meetingUuid);

        verifyNoInteractions(
                documentProcessingService,
                aiService,
                analysisRepository,
                meetingAnalysisMapper
        );
    }

    // No attachments
    @Test
    void shouldThrowExceptionWhenMeetingHasNoAttachments() {

        UUID meetingUuid = UUID.randomUUID();

        Meeting meeting = Meeting.builder()
                .uuid(meetingUuid)
                .attachments(new ArrayList<>())
                .build();

        AiAnalyzeRequest request =
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini-3.5-flash");

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.of(meeting));

        assertThatThrownBy(() ->
                meetingAnalysisService.analyze(meetingUuid, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No attachments found for meeting: " + meetingUuid);

        verify(meetingRepository).findByUuid(meetingUuid);

        verifyNoInteractions(
                documentProcessingService,
                aiService,
                analysisRepository,
                meetingAnalysisMapper
        );
    }

    // First analysis (Version = 1)
    @Test
    void shouldAnalyzeMeetingSuccessfully() {

        UUID meetingUuid = UUID.randomUUID();

        MeetingAttachment attachment =
                MeetingAttachment.builder()
                        .originalFileName("meeting.pdf")
                        .storedFileName("123.pdf")
                        .filePath("uploads/123.pdf")
                        .fileType("application/pdf")
                        .fileExtension("pdf")
                        .fileSize(100L)
                        .checksum("checksum")
                        .build();

        Meeting meeting = Meeting.builder()
                .uuid(meetingUuid)
                .attachments(List.of(attachment))
                .build();

        ProcessedDocument processedDocument =
                new ProcessedDocument(
                        "Meeting notes",
                        13,
                        2
                );

        AiAnalyzeRequest request =
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini-3.5-flash");

        AiAnalysisResult aiResult =
                new AiAnalysisResult(
                        "Summary",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        500L,
                        AiProvider.GEMINI,
                        "gemini-3.5-flash",
                        1
                );

        MeetingAnalysis savedAnalysis =
                MeetingAnalysis.builder()
                        .analysisVersion(1)
                        .summary("Summary")
                        .meeting(meeting)
                        .provider(AiProvider.GEMINI)
                        .model("gemini-3.5-flash")
                        .promptVersion(1)
                        .processingTimeMs(500L)
                        .status(AnalysisStatus.COMPLETED)
                        .build();

        MeetingAnalysisResponse response = mock(MeetingAnalysisResponse.class);

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.of(meeting));

        when(documentProcessingService.process(any()))
                .thenReturn(processedDocument);

        when(aiService.analyzeMeeting(any(), eq(request)))
                .thenReturn(aiResult);

        when(analysisRepository.findTopByMeetingOrderByAnalysisVersionDesc(meeting))
                .thenReturn(Optional.empty());

        when(analysisRepository.save(any(MeetingAnalysis.class)))
                .thenReturn(savedAnalysis);

        when(meetingAnalysisMapper.toResponse(savedAnalysis))
                .thenReturn(response);

        MeetingAnalysisResponse result =
                meetingAnalysisService.analyze(meetingUuid, request);

        assertThat(result).isEqualTo(response);

        verify(documentProcessingService).process(any());

        verify(aiService)
                .analyzeMeeting(any(ProcessedDocument.class), eq(request));

        verify(analysisRepository)
                .findTopByMeetingOrderByAnalysisVersionDesc(meeting);

        verify(analysisRepository)
                .save(any(MeetingAnalysis.class));

        verify(meetingAnalysisMapper)
                .toResponse(savedAnalysis);
    }

    // Retry analysis (Version increment)
    @Test
    void shouldIncrementAnalysisVersionWhenPreviousAnalysisExists() {

        UUID meetingUuid = UUID.randomUUID();

        MeetingAttachment attachment =
                MeetingAttachment.builder()
                        .originalFileName("meeting.pdf")
                        .storedFileName("meeting.pdf")
                        .filePath("path")
                        .fileType("application/pdf")
                        .fileExtension("pdf")
                        .fileSize(1L)
                        .checksum("checksum")
                        .build();

        Meeting meeting = Meeting.builder()
                .uuid(meetingUuid)
                .attachments(List.of(attachment))
                .build();

        MeetingAnalysis previous =
                MeetingAnalysis.builder()
                        .analysisVersion(3)
                        .build();

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.of(meeting));

        when(documentProcessingService.process(any()))
                .thenReturn(new ProcessedDocument("text", 4, 1));

        when(aiService.analyzeMeeting(any(), any()))
                .thenReturn(
                        new AiAnalysisResult(
                                "summary",
                                List.of(),
                                List.of(),
                                List.of(),
                                List.of(),
                                200L,
                                AiProvider.GEMINI,
                                "gemini",
                                1
                        )
                );

        when(analysisRepository.findTopByMeetingOrderByAnalysisVersionDesc(meeting))
                .thenReturn(Optional.of(previous));

        when(analysisRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(meetingAnalysisMapper.toResponse(any(MeetingAnalysis.class)))
                .thenReturn(mock(MeetingAnalysisResponse.class));

        meetingAnalysisService.analyze(
                meetingUuid,
                new AiAnalyzeRequest(AiProvider.GEMINI, "gemini")
        );

        ArgumentCaptor<MeetingAnalysis> captor =
                ArgumentCaptor.forClass(MeetingAnalysis.class);

        verify(analysisRepository).save(captor.capture());

        assertThat(captor.getValue().getAnalysisVersion())
                .isEqualTo(4);
    }
}

package com.sajee.meeting_mind_ai.ai.service;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.factory.AIModelStrategyFactory;
import com.sajee.meeting_mind_ai.ai.strategy.AiModelStrategy;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.analysis.model.ActionItem;
import com.sajee.meeting_mind_ai.common.exception.business.AiAnalysisException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiServiceImplTest {

    @Mock
    private AIModelStrategyFactory strategyFactory;

    @Mock
    private AiModelStrategy strategy;

    @InjectMocks
    private AiServiceImpl aiService;

    private ProcessedDocument document;
    private AiAnalyzeRequest request;
    private AiAnalysisResult result;

    @BeforeEach
    void setUp() {

        document = new ProcessedDocument(
                "Meeting text",
                12,
                2
        );

        request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-3.5-flash"
        );

        result = new AiAnalysisResult(
                "Summary",
                List.of(new ActionItem("John", "Finish authentication", "Friday")),
                List.of("Decision"),
                List.of("Risk"),
                List.of("Next Step"),
                1200L,
                AiProvider.GEMINI,
                "gemini-3.5-flash",
                1
        );
    }

    @Test
    void shouldAnalyzeMeetingSuccessfully() {

        when(strategyFactory.getStrategy(AiProvider.GEMINI))
                .thenReturn(strategy);

        when(strategy.analyze(document, request))
                .thenReturn(result);

        AiAnalysisResult response =
                aiService.analyzeMeeting(document, request);

        assertNotNull(response);
        assertEquals("Summary", response.summary());
        assertEquals(AiProvider.GEMINI, response.provider());

        verify(strategyFactory).getStrategy(AiProvider.GEMINI);
        verify(strategy).analyze(document, request);
    }

    @Test
    void shouldThrowAiAnalysisExceptionWhenStrategyThrowsAiException() {

        when(strategyFactory.getStrategy(AiProvider.GEMINI))
                .thenReturn(strategy);

        when(strategy.analyze(document, request))
                .thenThrow(new AiAnalysisException("AI failed"));

        assertThrows(
                AiAnalysisException.class,
                () -> aiService.analyzeMeeting(document, request)
        );

        verify(strategy).analyze(document, request);
    }

    @Test
    void shouldWrapUnexpectedException() {

        when(strategyFactory.getStrategy(AiProvider.GEMINI))
                .thenReturn(strategy);

        when(strategy.analyze(document, request))
                .thenThrow(new RuntimeException("Unexpected"));

        AiAnalysisException exception = assertThrows(
                AiAnalysisException.class,
                () -> aiService.analyzeMeeting(document, request)
        );

        assertEquals("AI analysis failed.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProviderNotSupported() {

        when(strategyFactory.getStrategy(AiProvider.GEMINI))
                .thenThrow(new IllegalArgumentException("Unsupported provider"));

        assertThrows(
                AiAnalysisException.class,
                () -> aiService.analyzeMeeting(document, request)
        );
    }
}
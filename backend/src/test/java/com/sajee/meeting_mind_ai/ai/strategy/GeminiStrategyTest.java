package com.sajee.meeting_mind_ai.ai.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.dto.response.GeminiResponse;
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
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiStrategyTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GeminiStrategy strategy;

    private ProcessedDocument document;
    private AiAnalyzeRequest request;

    @BeforeEach
    void setUp() {

        document = new ProcessedDocument(
                "Meeting notes...",
                16,
                2
        );

        request = new AiAnalyzeRequest(
                AiProvider.GEMINI,
                "gemini-3.5-flash"
        );
    }

    @Test
    void shouldAnalyzeSuccessfully() throws Exception {

        String json = """
                {
                  "summary":"Summary",
                  "actionItems":[],
                  "decisions":["Decision"],
                  "risks":["Risk"],
                  "nextSteps":["Next"]
                }
                """;

        GeminiResponse geminiResponse =
                new GeminiResponse(
                        "Summary",
                        List.of(
                                new ActionItem(
                                        "John",
                                        "Finish authentication",
                                        "Tomorrow"
                                )
                        ),
                        List.of("Decision"),
                        List.of("Risk"),
                        List.of("Next")
                );

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(json);

        when(objectMapper.readValue(json, GeminiResponse.class))
                .thenReturn(geminiResponse);

        AiAnalysisResult result =
                strategy.analyze(document, request);

        assertNotNull(result);
        assertEquals("Summary", result.summary());
        assertEquals(AiProvider.GEMINI, result.provider());
        assertEquals("gemini-3.5-flash", result.model());
        assertEquals(1, result.promptVersion());

        verify(chatClient).prompt();
        verify(requestSpec).user(anyString());
        verify(requestSpec).call();
        verify(responseSpec).content();
    }

    @Test
    void shouldThrowExceptionWhenGeminiReturnsEmptyResponse() {

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("");

        AiAnalysisException exception =
                assertThrows(
                        AiAnalysisException.class,
                        () -> strategy.analyze(document, request)
                );

        assertEquals(
                "Gemini returned an empty response.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenJsonIsInvalid() throws Exception {

        String invalidJson = "{invalid-json}";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(invalidJson);

        when(objectMapper.readValue(invalidJson, GeminiResponse.class))
                .thenThrow(mock(JsonProcessingException.class));

        AiAnalysisException exception =
                assertThrows(
                        AiAnalysisException.class,
                        () -> strategy.analyze(document, request)
                );

        assertEquals(
                "Gemini returned an invalid response.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenChatClientFails() {

        when(chatClient.prompt())
                .thenThrow(new RuntimeException("API unavailable"));

        AiAnalysisException exception =
                assertThrows(
                        AiAnalysisException.class,
                        () -> strategy.analyze(document, request)
                );

        assertEquals(
                "Failed to analyze document using Gemini.",
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnGeminiProvider() {

        assertEquals(AiProvider.GEMINI, strategy.provider());
    }
}
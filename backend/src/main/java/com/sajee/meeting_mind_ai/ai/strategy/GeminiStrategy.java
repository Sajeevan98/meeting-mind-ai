package com.sajee.meeting_mind_ai.ai.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.dto.response.GeminiResponse;
import com.sajee.meeting_mind_ai.ai.prompt.MeetingAnalysisPrompt;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.common.exception.business.AiAnalysisException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiStrategy implements AiModelStrategy {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

//    public GeminiStrategy(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }

    @Override
    public AiProvider provider() {

        return AiProvider.GEMINI;
    }

    @Override
    public AiAnalysisResult analyze(ProcessedDocument document, AiAnalyzeRequest request) {

        log.info("Starting AI analysis. Provider: {}, Model: {}", request.aiProvider(), request.model());

        Instant startTime = Instant.now();

        try {
            String prompt = MeetingAnalysisPrompt.ANALYSIS
                    .formatted(document.extractedText());

            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            long processingTime =
                    Duration.between(startTime, Instant.now()).toMillis();

            if (response == null || response.isBlank()) {
                throw new AiAnalysisException("Gemini returned an empty response.");
            }

            GeminiResponse geminiResponse =
                    objectMapper.readValue(response, GeminiResponse.class);

            return new AiAnalysisResult(
                    geminiResponse.summary(),
                    geminiResponse.actionItems(),
                    geminiResponse.decisions(),
                    geminiResponse.risks(),
                    geminiResponse.nextSteps(),
                    processingTime,
                    request.aiProvider(),
                    request.model(),
                    MeetingAnalysisPrompt.VERSION
            );
        } catch (AiAnalysisException ex) {

            throw ex;

        } catch (JsonProcessingException ex) {

            log.error("Invalid JSON returned from Gemini.", ex);
            throw new AiAnalysisException("Gemini returned an invalid response.", ex);

        } catch (Exception ex) {

            log.error("Gemini analysis failed. Model: {}", request.model(), ex);
            throw new AiAnalysisException("Failed to analyze document using Gemini.", ex);
        }
    }

//    @Override
//    public AIAnalysisResult analyze(ProcessedDocument document, AnalyzeMeetingRequest request) {
//
//        log.info("Starting AI analysis. Provider: {}, Model: {}", request.aiProvider(), request.model());
//
//        Instant startTime = Instant.now();
//
//        try {
//            String prompt = MeetingAnalysisPrompt.ANALYSIS
//                    .formatted(document.extractedText());
//
//            GeminiResponse geminiResponse = chatClient.prompt()
//                    .user(prompt)
//                    .call()
//                    .entity(GeminiResponse.class);
//
//            if (geminiResponse == null) {
//                throw new AIAnalysisException("Gemini returned an empty response.");
//            }
//
//            long processingTime =
//                    Duration.between(startTime, Instant.now()).toMillis();
//
//            return new AIAnalysisResult(
//                    geminiResponse.summary(),
//                    geminiResponse.actionItems(),
//                    geminiResponse.decisions(),
//                    geminiResponse.risks(),
//                    geminiResponse.nextSteps(),
//                    processingTime,
//                    request.aiProvider(),
//                    request.model(),
//                    MeetingAnalysisPrompt.VERSION
//            );
//        } catch (Exception ex) {
//
//            log.error("Gemini analysis failed. Model: {}", request.model(), ex);
//            throw new AIAnalysisException("Failed to analyze document using Gemini.", ex);
//        }
//    }

}

package com.sajee.meeting_mind_ai.ai.service;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.factory.AIModelStrategyFactory;
import com.sajee.meeting_mind_ai.ai.strategy.AiModelStrategy;
import com.sajee.meeting_mind_ai.common.exception.business.AiAnalysisException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final AIModelStrategyFactory strategyFactory;

    @Override
    public AiAnalysisResult analyzeMeeting(ProcessedDocument document, AiAnalyzeRequest request) {

        log.info("Starting AI analysis using provider: {}, model: {}", request.aiProvider(), request.model());

        try {
            AiModelStrategy strategy =
                    strategyFactory.getStrategy(request.aiProvider());

            log.info("Selected AI strategy: {}", strategy.getClass().getSimpleName());

            AiAnalysisResult result =
                    strategy.analyze(document, request);

            log.info("AI analysis completed successfully.");

            return result;

        } catch (AiAnalysisException ex) {
            throw ex;

        } catch (Exception ex) {

            log.error("Unexpected error while analyzing document.", ex);
            throw new AiAnalysisException("AI analysis failed.", ex);
        }
    }
}

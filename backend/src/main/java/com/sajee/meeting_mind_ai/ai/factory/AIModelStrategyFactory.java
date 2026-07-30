package com.sajee.meeting_mind_ai.ai.factory;

import com.sajee.meeting_mind_ai.ai.strategy.AiModelStrategy;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.common.exception.business.AiAnalysisException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AIModelStrategyFactory {

    private final List<AiModelStrategy> strategies;

    public AiModelStrategy getStrategy(AiProvider provider) {

        return strategies.stream()
                .filter(strategy -> strategy.provider() == provider)
                .findFirst()
                .orElseThrow(() ->
                        new AiAnalysisException("Unsupported AI provider: " + provider));
    }
}
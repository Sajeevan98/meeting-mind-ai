package com.sajee.meeting_mind_ai.ai.factory;

import com.sajee.meeting_mind_ai.ai.strategy.AiModelStrategy;
import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import com.sajee.meeting_mind_ai.common.exception.business.AiAnalysisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIModelStrategyFactoryTest {

    private AIModelStrategyFactory factory;

    private AiModelStrategy geminiStrategy;
    private AiModelStrategy openAiStrategy;

    @BeforeEach
    void setUp() {

        geminiStrategy = mock(AiModelStrategy.class);
        openAiStrategy = mock(AiModelStrategy.class);

        when(geminiStrategy.provider())
                .thenReturn(AiProvider.GEMINI);

        when(openAiStrategy.provider())
                .thenReturn(AiProvider.OPENAI);

        factory = new AIModelStrategyFactory(
                List.of(geminiStrategy, openAiStrategy)
        );
    }

    @Test
    void shouldReturnGeminiStrategy() {

        AiModelStrategy strategy =
                factory.getStrategy(AiProvider.GEMINI);

        assertNotNull(strategy);
        assertSame(geminiStrategy, strategy);
    }

    @Test
    void shouldReturnOpenAiStrategy() {

        AiModelStrategy strategy =
                factory.getStrategy(AiProvider.OPENAI);

        assertNotNull(strategy);
        assertSame(openAiStrategy, strategy);
    }

    @Test
    void shouldThrowExceptionWhenProviderNotSupported() {

        factory = new AIModelStrategyFactory(List.of(geminiStrategy));

        assertThrows(
                AiAnalysisException.class,
                () -> factory.getStrategy(AiProvider.OPENAI)
        );
    }
}

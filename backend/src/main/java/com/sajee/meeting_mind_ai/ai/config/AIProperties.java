package com.sajee.meeting_mind_ai.ai.config;

import com.sajee.meeting_mind_ai.analysis.enums.AiProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "app.ai")
public record AIProperties(

        AiProvider provider,

        String model
) {
}

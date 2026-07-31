package com.sajee.meeting_mind_ai.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private String allowedOrigin;
}

package com.sajee.meeting_mind_ai.storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.file")
public class StorageProperties {

    private String uploadDir;

    private Long maxSize;

    private List<String> allowedTypes;
}

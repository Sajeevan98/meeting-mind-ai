package com.sajee.meeting_mind_ai.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    ChatClient chatClient(ChatModel model) {

        return ChatClient.builder(model).build();
    }

    // JacksonConfig
    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }
}

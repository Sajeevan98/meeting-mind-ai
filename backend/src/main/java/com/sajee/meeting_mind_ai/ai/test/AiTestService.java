//package com.sajee.meeting_mind_ai.ai.test;
//
//import com.google.genai.Client;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AiTestService {
//
//    private final ChatClient chatClient;
//
//    public String contactAi(String msg) {
//
//        log.info("AI SERVICE START ===> {}", msg);
//
//        // Check available models to my-api-key
//        Client client = Client.builder()
//                .apiKey("GEMINI_API_KEY")
//                .build();
//        client.models.list(null).forEach(
//                m-> System.out.println(m.name() + "\n")
//        );
//
//        return chatClient.prompt()
//                .user(msg)
//                .call()
//                .content();
//    }
//}

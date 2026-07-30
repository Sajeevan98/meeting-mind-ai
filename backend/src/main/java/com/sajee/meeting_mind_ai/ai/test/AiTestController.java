//package com.sajee.meeting_mind_ai.ai.test;
//
//import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
//import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
//import com.sajee.meeting_mind_ai.ai.service.AiService;
//import com.sajee.meeting_mind_ai.common.response.ApiResponse;
//import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/test")
//@RequiredArgsConstructor
//public class AiTestController {
//
//    private final AiTestService aiTestService;
//    private final AiService aiService;
//
//    @GetMapping
//    public ResponseEntity<String> testAi() {
//
//        return ResponseEntity.ok(aiTestService.contactAi("Explain Spring Boot in 3 sentences?"));
//    }
//
//    @PostMapping("/analyze")
//    public ApiResponse<AiAnalysisResult> analyze(@RequestBody AiAnalyzeTestRequest request) {
//
//        ProcessedDocument document = new ProcessedDocument(
//                request.text(),
//                request.text().length(),
//                request.text().isBlank()
//                        ? 0
//                        : request.text().trim().split("\\s+").length
//        );
//
//        AiAnalyzeRequest analyzeRequest =
//                new AiAnalyzeRequest(
//                        request.aiProvider(),
//                        request.model()
//                );
//
//        AiAnalysisResult result = aiService.analyzeMeeting(document, analyzeRequest);
//
//        return ApiResponse.success("AI analysis completed successfully.", result);
//    }
//}
///*
//    Sample JSON to Postman: via POST Request
//    {
//      "text": "Weekly meeting. John will finish authentication by Friday. Sarah will contact the API vendor. The team agreed to postpone reporting until next sprint.",
//      "aiProvider": "GEMINI",
//      "model": "gemini-3.1-flash-lite"
//    }
//*/
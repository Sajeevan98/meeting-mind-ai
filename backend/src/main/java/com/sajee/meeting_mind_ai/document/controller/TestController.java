package com.sajee.meeting_mind_ai.document.controller;

import com.sajee.meeting_mind_ai.common.response.ApiResponse;
import com.sajee.meeting_mind_ai.common.util.ApiEndpoints;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.document.service.DocumentProcessingService;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import com.sajee.meeting_mind_ai.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiEndpoints.DOCUMENTS)
@RequiredArgsConstructor
public class TestController {

    private final FileStorageService fileStorageService;
    private final DocumentProcessingService documentProcessingService;

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<ProcessedDocument>> processDocument(
            @RequestParam("file") MultipartFile file
    ) {

        StoredFile storedFile = fileStorageService.store(file);

        ProcessedDocument response = documentProcessingService.process(storedFile);

        return ResponseEntity.ok(
                ApiResponse.success("Document processed successfully.", response)
        );
    }

}

package com.sajee.meeting_mind_ai.storage.controller;

import com.sajee.meeting_mind_ai.common.response.ApiResponse;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import com.sajee.meeting_mind_ai.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    // --------------------------
    // Temporary Test Controller
    // --------------------------

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<StoredFile> upload(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(fileStorageService.store(file));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam String filePath) {

        fileStorageService.delete(filePath);

        return ResponseEntity.ok(
                ApiResponse.success("Deleted file: " + filePath)
        );
    }
}

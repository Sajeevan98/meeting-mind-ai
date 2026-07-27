package com.sajee.meeting_mind_ai.storage.service;

import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    StoredFile store(MultipartFile file);

    void delete(String filePath);
}

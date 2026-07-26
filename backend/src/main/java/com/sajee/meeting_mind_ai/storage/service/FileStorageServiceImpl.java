package com.sajee.meeting_mind_ai.storage.service;

import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService{

    @Override
    public StoredFile store(MultipartFile file) {
        return null;
    }

    @Override
    public void delete(String filePath) {

    }
}

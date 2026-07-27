package com.sajee.meeting_mind_ai.document.extractor;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class TextDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String fileExtension) {

        return "txt".equalsIgnoreCase(fileExtension);
    }

    @Override
    public String extract(Path filePath) {

        log.info("Extracting text from TXT file: {}", filePath);

        try {
            return Files.readString(filePath);

        } catch (IOException ex) {

            log.error("Failed to extract TXT document: {}", filePath, ex);

            throw new DocumentProcessingException("Unable to process TXT document.", ex);
        }
    }
}

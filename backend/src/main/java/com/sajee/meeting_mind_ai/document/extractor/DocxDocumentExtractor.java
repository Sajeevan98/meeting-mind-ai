package com.sajee.meeting_mind_ai.document.extractor;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class DocxDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String extension) {
        return "docx".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(Path filePath) {

        log.info("Extracting text from DOCX: {}", filePath);

        try (
                XWPFDocument document = new XWPFDocument(Files.newInputStream(filePath));
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        ) {

            return extractor.getText();

        } catch (IOException ex) {

            log.error("Failed to extract DOCX: {}", filePath, ex);

            throw new DocumentProcessingException("Unable to process DOCX document.", ex);
        }
    }
}
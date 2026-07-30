package com.sajee.meeting_mind_ai.document.extractor;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String extension) {

        return "doc".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(Path file) {

        try (
                InputStream in = Files.newInputStream(file);
                HWPFDocument document = new HWPFDocument(in)
        ) {

            WordExtractor extractor = new WordExtractor(document);

            return extractor.getText();

        } catch (IOException ex) {

            throw new DocumentProcessingException("Failed to read DOC document.", ex);
        }
    }
}

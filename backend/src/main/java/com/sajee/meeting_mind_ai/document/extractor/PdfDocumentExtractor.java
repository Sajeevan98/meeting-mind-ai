package com.sajee.meeting_mind_ai.document.extractor;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j
public class PdfDocumentExtractor implements DocumentExtractor {

    @Override
    public boolean supports(String extension) {

        return "pdf".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(Path filePath) {

        log.info("Extracting text from PDF: {}", filePath);

        try (PDDocument document = Loader.loadPDF(filePath.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch (IOException ex) {

            log.error("Failed to extract PDF: {}", filePath, ex);

            throw new DocumentProcessingException("Unable to process PDF document.", ex);
        }
    }
}
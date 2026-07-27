package com.sajee.meeting_mind_ai.document.service;

import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.document.extractor.DocumentExtractor;
import com.sajee.meeting_mind_ai.document.factory.DocumentExtractorFactory;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentProcessingServiceImpl implements DocumentProcessingService {

    private final DocumentExtractorFactory extractorFactory;

    @Override
    public ProcessedDocument process(StoredFile storedFile) {

        log.info("Processing document: {}", storedFile.originalFileName());

        DocumentExtractor extractor =
                extractorFactory.getExtractor(storedFile.fileExtension());

        String extractedText = extractor.extract(Path.of(storedFile.filePath()));

        String cleanedText = cleanExtractedText(extractedText);

        int characterCount = cleanedText.length();

        int wordCount = cleanedText.isBlank()
                ? 0
                : cleanedText.trim().split("\\s+").length;

        log.info(
                "Document processed successfully. Characters: {}, Words: {}",
                characterCount,
                wordCount
        );

        return new ProcessedDocument(
                extractedText,
                characterCount,
                wordCount
        );
    }

    private String cleanExtractedText(String text) {

        return text
                .replace("\r", "")
                .replace("\t", " ")
                .replaceAll("[ ]{2,}", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }
}

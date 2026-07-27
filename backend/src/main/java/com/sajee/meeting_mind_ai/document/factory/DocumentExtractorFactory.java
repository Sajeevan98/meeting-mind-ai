package com.sajee.meeting_mind_ai.document.factory;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import com.sajee.meeting_mind_ai.document.extractor.DocumentExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentExtractorFactory {

    private final List<DocumentExtractor> extractors;

    public DocumentExtractor getExtractor(String extension) {

        return extractors.stream()
                .filter(extractor -> extractor.supports(extension))
                .findFirst()
                .orElseThrow(() ->
                        new DocumentProcessingException("Unsupported document type: " + extension));
    }
}
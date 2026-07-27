package com.sajee.meeting_mind_ai.document.service;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.document.extractor.DocumentExtractor;
import com.sajee.meeting_mind_ai.document.factory.DocumentExtractorFactory;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentProcessingServiceTest {

    @Mock
    private DocumentExtractorFactory extractorFactory;

    @Mock
    private DocumentExtractor extractor;

    @InjectMocks
    private DocumentProcessingServiceImpl service;

    private StoredFile storedFile;

    @BeforeEach
    void setUp() {

        storedFile = new StoredFile(
                "meeting.txt",
                "123.txt",
                "uploads/meetings/123.txt",
                "text/plain",
                "txt",
                100L,
                "checksum"
        );
    }

    @Test
    void shouldProcessDocumentSuccessfully() {

        when(extractorFactory.getExtractor("txt"))
                .thenReturn(extractor);

        when(extractor.extract(Path.of("uploads/meetings/123.txt")))
                .thenReturn("Hello World");

        ProcessedDocument result = service.process(storedFile);

        assertNotNull(result);
        assertEquals("Hello World", result.extractedText());
        assertEquals(11, result.characterCount());
        assertEquals(2, result.wordCount());

        verify(extractorFactory).getExtractor("txt");
        verify(extractor).extract(Path.of("uploads/meetings/123.txt"));
    }

    @Test
    void shouldCalculateCharacterCountCorrectly() {

        when(extractorFactory.getExtractor(anyString()))
                .thenReturn(extractor);

        when(extractor.extract(any(Path.class)))
                .thenReturn("Spring Boot");

        ProcessedDocument result = service.process(storedFile);

        assertEquals(11, result.characterCount());
    }

    @Test
    void shouldCalculateWordCountCorrectly() {

        when(extractorFactory.getExtractor(anyString()))
                .thenReturn(extractor);

        when(extractor.extract(any(Path.class)))
                .thenReturn("Spring Boot React AI");

        ProcessedDocument result = service.process(storedFile);

        assertEquals(4, result.wordCount());
    }

    @Test
    void shouldReturnZeroWordsForBlankDocument() {

        when(extractorFactory.getExtractor(anyString()))
                .thenReturn(extractor);

        when(extractor.extract(any(Path.class)))
                .thenReturn("   ");

        ProcessedDocument result = service.process(storedFile);

        assertEquals(0, result.wordCount());
        assertEquals(0, result.characterCount());
    }

    @Test
    void shouldPropagateExtractionException() {

        when(extractorFactory.getExtractor(anyString()))
                .thenReturn(extractor);

        when(extractor.extract(any(Path.class)))
                .thenThrow(new DocumentProcessingException("Extraction failed"));

        assertThrows(
                DocumentProcessingException.class,
                () -> service.process(storedFile)
        );
    }

    @Test
    void shouldUseCorrectExtractor() {

        when(extractorFactory.getExtractor("txt"))
                .thenReturn(extractor);

        when(extractor.extract(any(Path.class)))
                .thenReturn("Hello");

        service.process(storedFile);

        verify(extractorFactory).getExtractor("txt");
        verify(extractor).extract(Path.of("uploads/meetings/123.txt"));
    }
}

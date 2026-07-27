package com.sajee.meeting_mind_ai.document.factory;

import com.sajee.meeting_mind_ai.common.exception.business.DocumentProcessingException;
import com.sajee.meeting_mind_ai.document.extractor.DocumentExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentExtractorFactoryTest {

    private DocumentExtractorFactory factory;

    private DocumentExtractor textExtractor;
    private DocumentExtractor pdfExtractor;
    private DocumentExtractor docxExtractor;

    @BeforeEach
    void setUp() {

        textExtractor = mock(DocumentExtractor.class);
        pdfExtractor = mock(DocumentExtractor.class);
        docxExtractor = mock(DocumentExtractor.class);

        when(textExtractor.supports("txt")).thenReturn(true);

        when(pdfExtractor.supports("pdf")).thenReturn(true);

        when(docxExtractor.supports("docx")).thenReturn(true);

        factory = new DocumentExtractorFactory(
                List.of(textExtractor, pdfExtractor, docxExtractor)
        );
    }

    @Test
    void shouldReturnTextExtractor() {

        DocumentExtractor extractor = factory.getExtractor("txt");

        assertSame(textExtractor, extractor);
    }

    @Test
    void shouldReturnPdfExtractor() {

        DocumentExtractor extractor = factory.getExtractor("pdf");

        assertSame(pdfExtractor, extractor);
    }

    @Test
    void shouldReturnDocxExtractor() {

        DocumentExtractor extractor = factory.getExtractor("docx");

        assertSame(docxExtractor, extractor);
    }

    @Test
    void shouldThrowExceptionForUnsupportedExtension() {

        assertThrows(
                DocumentProcessingException.class,
                () -> factory.getExtractor("zip")
        );
    }
}
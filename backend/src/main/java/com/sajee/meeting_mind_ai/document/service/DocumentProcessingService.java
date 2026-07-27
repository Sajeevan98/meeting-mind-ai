package com.sajee.meeting_mind_ai.document.service;

import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;

public interface DocumentProcessingService {

    ProcessedDocument process(StoredFile storedFile);
}

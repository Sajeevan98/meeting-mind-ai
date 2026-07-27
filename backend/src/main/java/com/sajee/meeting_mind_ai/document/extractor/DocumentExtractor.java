package com.sajee.meeting_mind_ai.document.extractor;

import java.nio.file.Path;

public interface DocumentExtractor {

    boolean supports(String fileExtension);

    String extract(Path filePath);
}
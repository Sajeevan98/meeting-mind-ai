package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class FileSizeExceededException extends BusinessException {

    public FileSizeExceededException(Long maxSize, Long fileSize) {

        super("Maximum size: " + maxSize + ", Uploaded file size: " + fileSize);
    }
}

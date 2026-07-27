package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class FileStorageException extends BusinessException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

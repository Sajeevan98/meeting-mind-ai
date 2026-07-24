package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class FileStorageException extends BusinessException {

    public FileStorageException(String msg){

        super(msg);
    }

    public FileStorageException(String msg, Throwable cause){

        super(msg, cause);
    }
}

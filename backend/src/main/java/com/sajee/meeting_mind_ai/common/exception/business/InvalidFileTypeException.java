package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class InvalidFileTypeException extends BusinessException {

    public InvalidFileTypeException(String type) {

        super("Unsupported file type: " + type);
    }
}

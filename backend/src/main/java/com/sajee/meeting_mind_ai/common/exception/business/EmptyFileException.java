package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class EmptyFileException extends BusinessException {

    public EmptyFileException() {

        super("Uploaded file is empty!");
    }
}

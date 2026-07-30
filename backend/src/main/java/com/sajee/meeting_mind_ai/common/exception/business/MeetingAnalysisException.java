package com.sajee.meeting_mind_ai.common.exception.business;

import com.sajee.meeting_mind_ai.common.exception.BusinessException;

public class MeetingAnalysisException extends BusinessException {

    public MeetingAnalysisException(String message) {

        super(message);
    }

    public MeetingAnalysisException(String message, Throwable cause) {

        super(message, cause);
    }
}

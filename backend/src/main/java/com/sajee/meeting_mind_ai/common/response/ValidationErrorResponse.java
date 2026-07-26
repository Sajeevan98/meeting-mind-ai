package com.sajee.meeting_mind_ai.common.response;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(

        boolean success,

        int status,

        String error,

        String message,

        Map<String, String> errors,

        Instant timestamp
) {
}

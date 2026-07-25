package com.sajee.meeting_mind_ai.common.response;

import java.time.Instant;

public record ErrorResponse(

        boolean success,

        int status,

        String error,

        String message,

        Instant timestamp
) {
}

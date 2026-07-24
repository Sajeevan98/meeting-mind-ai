package com.sajee.meeting_mind_ai.common.response;

import java.time.LocalDateTime;

public record ErrorResponse(

        boolean success,
        int status,
        String message,
        LocalDateTime timestamp
) { }

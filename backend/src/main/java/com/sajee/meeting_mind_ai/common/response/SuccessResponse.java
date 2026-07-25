package com.sajee.meeting_mind_ai.common.response;

import java.time.Instant;

public record SuccessResponse<T>(

        boolean success,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>(
                true,
                message,
                data,
                Instant.now()
        );
    }

}

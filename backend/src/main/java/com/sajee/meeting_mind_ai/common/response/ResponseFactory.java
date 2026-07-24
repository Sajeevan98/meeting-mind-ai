package com.sajee.meeting_mind_ai.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    private ResponseFactory() {

    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(String message, T data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.success(message, data));
    }


    public static <T> ResponseEntity<SuccessResponse<T>> ok(String message, T data) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(message, data));
    }

    public static ResponseEntity<Void> noContent() {

        return ResponseEntity.noContent().build();
    }
}

package com.sajee.meeting_mind_ai.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {

        super(message);
    }

    public static ResourceNotFoundException forEntity(String entity, Object id) {

        return new ResourceNotFoundException(
                entity + " not found with id: " + id
        );
    }
}

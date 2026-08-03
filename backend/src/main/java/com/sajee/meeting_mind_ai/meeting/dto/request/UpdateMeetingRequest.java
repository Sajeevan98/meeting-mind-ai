package com.sajee.meeting_mind_ai.meeting.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMeetingRequest(

        @NotBlank(message = "Meeting title is required.")
        @Size(max = 255, message = "Meeting title cannot exceed 255 characters.")
        String title,

        @NotBlank(message = "Meeting description is required.")
        @Size(max = 1000, message = "Meeting description cannot exceed 1000 characters.")
        String description
) {
}

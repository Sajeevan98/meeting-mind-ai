package com.sajee.meeting_mind_ai.meeting.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record MeetingPageResponse<T>(

        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> MeetingPageResponse<T> from(Page<T> page) {

        return new MeetingPageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}

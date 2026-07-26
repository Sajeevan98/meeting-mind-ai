package com.sajee.meeting_mind_ai.meeting.service;

import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MeetingService {

    MeetingResponse create(CreateMeetingRequest request);

    MeetingResponse getByUuid(UUID uuid);

    Page<MeetingResponse> getAll(Pageable pageable);

    MeetingResponse update(UUID uuid, UpdateMeetingRequest request);

    void delete(UUID uuid);
}

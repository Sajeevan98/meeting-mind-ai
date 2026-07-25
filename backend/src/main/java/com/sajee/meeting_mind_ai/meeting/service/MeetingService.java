package com.sajee.meeting_mind_ai.meeting.service;

import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;

import java.util.List;
import java.util.UUID;

public interface MeetingService {

    MeetingResponse create(CreateMeetingRequest request);

    MeetingResponse getByUuid(UUID uuid);

    List<MeetingResponse> getAll();

    MeetingResponse update(UUID uuid, UpdateMeetingRequest request);

    void delete(UUID uuid);
}

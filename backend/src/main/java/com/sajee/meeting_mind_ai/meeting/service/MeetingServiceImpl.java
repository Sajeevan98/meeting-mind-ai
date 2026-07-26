package com.sajee.meeting_mind_ai.meeting.service;

import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.mapper.MeetingMapper;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;

    @Override
    @Transactional
    public MeetingResponse create(CreateMeetingRequest request) {

        Meeting meeting = meetingMapper.toEntity(request);
        Meeting savedMeeting = meetingRepository.save(meeting);

        return meetingMapper.toResponse(savedMeeting);
    }

    @Override
    public MeetingResponse getByUuid(UUID uuid) {

        Meeting meeting = meetingRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with UUID: " + uuid));

        return meetingMapper.toResponse(meeting);
    }

    @Override
    public Page<MeetingResponse> getAll(Pageable pageable) {

        return meetingRepository
                .findAll(pageable)
                .map(meetingMapper::toResponse);
    }

    @Override
    @Transactional
    public MeetingResponse update(UUID uuid, UpdateMeetingRequest request) {

        Meeting meeting = meetingRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with UUID: " + uuid));

        meeting.setTitle(request.title());

        // return meetingMapper.toResponse(meeting); // Hibernate performs Dirty Checking.

        Meeting updated = meetingRepository.save(meeting);
        return meetingMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {

        Meeting meeting = meetingRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with UUID: " + uuid));

        meetingRepository.delete(meeting);
    }
}

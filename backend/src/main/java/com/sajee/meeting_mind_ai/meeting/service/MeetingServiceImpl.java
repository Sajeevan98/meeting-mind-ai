package com.sajee.meeting_mind_ai.meeting.service;

import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import com.sajee.meeting_mind_ai.attachment.repository.MeetingAttachmentRepository;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.mapper.MeetingMapper;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;
    private final MeetingAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

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

        log.info("Deleting meeting with attachments: {}", uuid);

        Meeting meeting = meetingRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with UUID: " + uuid));

        List<MeetingAttachment> attachment = attachmentRepository.findByMeetingUuid(uuid);

        log.info("Found {} attachments", attachment.size());

        // When meeting is deleted, attachments also will delete from the file-storage-directory
        attachment.forEach(attach ->{

            log.info("Deleting attachment: {}", attach.getFilePath());
            fileStorageService.delete(attach.getFilePath());
        });

        meetingRepository.delete(meeting);
    }
}

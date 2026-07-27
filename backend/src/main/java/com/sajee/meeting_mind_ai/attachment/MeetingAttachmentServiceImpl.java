package com.sajee.meeting_mind_ai.attachment;

import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import com.sajee.meeting_mind_ai.attachment.mapper.MeetingAttachmentMapper;
import com.sajee.meeting_mind_ai.attachment.repository.MeetingAttachmentRepository;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import com.sajee.meeting_mind_ai.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingAttachmentServiceImpl implements MeetingAttachmentService {

    private final MeetingRepository meetingRepository;
    private final MeetingAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;
    private final MeetingAttachmentMapper attachmentMapper;

    @Override
    public AttachmentResponse upload(UUID meetingUuid, MultipartFile file) {

        log.info("Uploading attachment for meeting: {}", meetingUuid);

        Meeting meeting = meetingRepository.findByUuid(meetingUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found: " + meetingUuid));

        StoredFile storedFile = fileStorageService.store(file);

        try {
            MeetingAttachment attachment = MeetingAttachment.builder()
                    .originalFileName(storedFile.originalFileName())
                    .storedFileName(storedFile.storedFileName())
                    .filePath(storedFile.filePath())
                    .fileType(storedFile.fileType())
                    .fileExtension(storedFile.fileExtension())
                    .fileSize(storedFile.fileSize())
                    .checksum(storedFile.checksum())
                    .build();

            // Instead of: attachment.setMeeting(meeting);
            // because helper method of MeetingEntity does both sides
            meeting.addAttachment(attachment);

            MeetingAttachment savedAttachment = attachmentRepository.save(attachment);
            log.info("Attachment uploaded successfully to Meeting UUID: {}", savedAttachment.getMeeting().getUuid());

            return attachmentMapper.toResponse(savedAttachment);

        } catch (Exception ex) {

            log.error("Database save failed. Removing uploaded file: {}", storedFile.filePath(), ex);

            fileStorageService.delete(storedFile.filePath());

            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentResponse> getByMeeting(UUID meetingUuid) {

        log.info("Fetching attachments for meeting: {}", meetingUuid);

        if (!meetingRepository.existsByUuid(meetingUuid)) {

            log.warn("Meeting not found: {}", meetingUuid);
            throw new ResourceNotFoundException("Meeting not found with UUID: " + meetingUuid);
        }

        List<MeetingAttachment> attachments =
                attachmentRepository.findAllByMeetingUuidOrderByCreatedAtDesc(meetingUuid);

        log.info("Found {} attachment(s)", attachments.size());

        return attachmentMapper.toResponse(attachments);
    }

    @Override
    public void delete(UUID attachmentUuid) {

        log.info("Deleting attachment: {}", attachmentUuid);

        MeetingAttachment attachment = attachmentRepository.findByUuid(attachmentUuid)
                .orElseThrow(() -> {

                    log.warn("Attachment not found: {}", attachmentUuid);
                    return new ResourceNotFoundException("Attachment not found with UUID: " + attachmentUuid);
                });

        log.warn("Attachment is deleted with path: {}", attachment.getFilePath());
        fileStorageService.delete(attachment.getFilePath());

        attachmentRepository.delete(attachment);

        log.info("Attachment deleted successfully: {}", attachmentUuid);
    }
}

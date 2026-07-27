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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingAttachmentServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MeetingAttachmentRepository attachmentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MeetingAttachmentMapper attachmentMapper;

    @InjectMocks
    private MeetingAttachmentServiceImpl attachmentService;

    @Test
    void shouldUploadAttachmentSuccessfully() {

        UUID meetingUuid = UUID.randomUUID();

        Meeting meeting = Meeting.builder()
                .uuid(meetingUuid)
                .title("Sprint Planning")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "meeting.txt",
                "text/plain",
                "Hello".getBytes()
        );

        StoredFile storedFile = new StoredFile(
                "meeting.txt",
                "123.txt",
                "uploads/meetings/123.txt",
                "text/plain",
                "txt",
                5L,
                "checksum"
        );

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.of(meeting));

        when(fileStorageService.store(file))
                .thenReturn(storedFile);

        when(attachmentRepository.save(any(MeetingAttachment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AttachmentResponse response =
                new AttachmentResponse(
                        UUID.randomUUID(),
                        "meeting.txt",
                        "123.txt",
                        "text/plain",
                        "txt",
                        5L,
                        Instant.now()
                );

        when(attachmentMapper.toResponse(any(MeetingAttachment.class)))
                .thenReturn(response);

        AttachmentResponse result =
                attachmentService.upload(meetingUuid, file);

        assertThat(result).isNotNull();
        assertThat(result.originalFileName())
                .isEqualTo("meeting.txt");

        verify(fileStorageService).store(file);
        verify(attachmentRepository).save(any(MeetingAttachment.class));
        verify(attachmentMapper).toResponse(any(MeetingAttachment.class));
    }

    @Test
    void shouldThrowExceptionWhenMeetingNotFound() {

        UUID uuid = UUID.randomUUID();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "meeting.txt",
                        "text/plain",
                        "Hello".getBytes()
                );

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                attachmentService.upload(uuid, file))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(fileStorageService, never()).store(any());
    }

    @Test
    void shouldDeleteUploadedFileWhenDatabaseSaveFails() {

        UUID meetingUuid = UUID.randomUUID();

        Meeting meeting = Meeting.builder()
                .uuid(meetingUuid)
                .build();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "meeting.txt",
                        "text/plain",
                        "Hello".getBytes()
                );

        StoredFile storedFile =
                new StoredFile(
                        "meeting.txt",
                        "123.txt",
                        "uploads/meetings/123.txt",
                        "text/plain",
                        "txt",
                        5L,
                        "checksum"
                );

        when(meetingRepository.findByUuid(meetingUuid))
                .thenReturn(Optional.of(meeting));

        when(fileStorageService.store(file))
                .thenReturn(storedFile);

        when(attachmentRepository.save(any()))
                .thenThrow(new RuntimeException("DB Error"));

        assertThatThrownBy(() ->
                attachmentService.upload(meetingUuid, file))
                .isInstanceOf(RuntimeException.class);

        verify(fileStorageService)
                .delete("uploads/meetings/123.txt");
    }

    @Test
    void shouldGetAttachmentsByMeetingSuccessfully() {

        UUID meetingUuid = UUID.randomUUID();

        when(meetingRepository.existsByUuid(meetingUuid))
                .thenReturn(true);

        List<MeetingAttachment> attachments =
                List.of(new MeetingAttachment());

        when(attachmentRepository.findAllByMeetingUuidOrderByCreatedAtDesc(meetingUuid))
                .thenReturn(attachments);

        List<AttachmentResponse> responses =
                List.of(mock(AttachmentResponse.class));

        when(attachmentMapper.toResponse(attachments))
                .thenReturn(responses);

        List<AttachmentResponse> result =
                attachmentService.getByMeeting(meetingUuid);

        assertThat(result).hasSize(1);

        verify(attachmentRepository)
                .findAllByMeetingUuidOrderByCreatedAtDesc(meetingUuid);
    }

    @Test
    void shouldThrowExceptionWhenMeetingDoesNotExist() {

        UUID uuid = UUID.randomUUID();

        when(meetingRepository.existsByUuid(uuid))
                .thenReturn(false);

        assertThatThrownBy(() ->
                attachmentService.getByMeeting(uuid))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(attachmentRepository, never())
                .findAllByMeetingUuidOrderByCreatedAtDesc(any());
    }

    @Test
    void shouldDeleteAttachmentSuccessfully() {

        UUID uuid = UUID.randomUUID();

        MeetingAttachment attachment =
                MeetingAttachment.builder()
                        .filePath("uploads/meetings/test.pdf")
                        .build();

        when(attachmentRepository.findByUuid(uuid))
                .thenReturn(Optional.of(attachment));

        attachmentService.delete(uuid);

        verify(fileStorageService)
                .delete("uploads/meetings/test.pdf");

        verify(attachmentRepository)
                .delete(attachment);
    }

    @Test
    void shouldThrowExceptionWhenAttachmentNotFound() {

        UUID uuid = UUID.randomUUID();

        when(attachmentRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                attachmentService.delete(uuid))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(fileStorageService, never())
                .delete(anyString());

        verify(attachmentRepository, never())
                .delete(any());
    }

    @Test
    void shouldThrowExceptionWhenFileDeletionFails() {

        UUID uuid = UUID.randomUUID();

        MeetingAttachment attachment =
                MeetingAttachment.builder()
                        .filePath("uploads/meetings/test.pdf")
                        .build();

        when(attachmentRepository.findByUuid(uuid))
                .thenReturn(Optional.of(attachment));

        doThrow(new RuntimeException("File delete failed"))
                .when(fileStorageService)
                .delete(anyString());

        assertThatThrownBy(() ->
                attachmentService.delete(uuid))
                .isInstanceOf(RuntimeException.class);

        verify(attachmentRepository, never())
                .delete(any());
    }

}

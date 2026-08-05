package com.sajee.meeting_mind_ai.integration.attachment;

import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import com.sajee.meeting_mind_ai.attachment.repository.MeetingAttachmentRepository;
import com.sajee.meeting_mind_ai.attachment.service.MeetingAttachmentService;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// run cmd: mvn -Dtest=MeetingAttachmentServiceIntegrationTest test

@SpringBootTest
@ActiveProfiles("test")
public class MeetingAttachmentServiceIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MeetingAttachmentService attachmentService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingAttachmentRepository attachmentRepository;

    @Test
    void shouldUploadAttachmentSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello MeetingMind AI".getBytes()
        );

        // Act
        AttachmentResponse response = attachmentService.upload(meeting.uuid(), file);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.uuid()).isNotNull();
        assertThat(response.originalFileName())
                .isEqualTo("test.txt");
        assertThat(response.fileType())
                .isEqualTo("text/plain");
        assertThat(response.fileSize())
                .isEqualTo((long) "Hello MeetingMind AI".getBytes().length);

        assertThat(attachmentRepository.findByUuid(response.uuid())).isPresent();
    }

    @Test
    void shouldGetAttachmentsSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "meeting.txt",
                "text/plain",
                "Meeting attachment content".getBytes()
        );
        attachmentService.upload(meeting.uuid(), file);

        // Act
        List<AttachmentResponse> attachments = attachmentService.getByMeeting(meeting.uuid());

        // Assert
        assertThat(attachments).hasSize(1);

        assertThat(attachments.get(0).originalFileName()).isEqualTo("meeting.txt");
    }

    @Test
    void shouldDeleteAttachmentSuccessfully() {

        // Arrange
        MeetingResponse meeting = createMeeting();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "delete-me.txt",
                "text/plain",
                "Delete this file".getBytes()
        );
        AttachmentResponse uploaded = attachmentService.upload(meeting.uuid(), file);

        // Verify it exists first
        assertThat(attachmentRepository.findByUuid(uploaded.uuid())).isPresent();

        // Act
        attachmentService.delete(uploaded.uuid());

        // Assert
        assertThat(attachmentRepository.findByUuid(uploaded.uuid())).isEmpty();
    }

    // if meeting not exists
    @Test
    void shouldThrowExceptionWhenMeetingNotFound() {

        // Arrange
        UUID meetingUuid = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test".getBytes()
        );

        // Act & Assert
        assertThatThrownBy(() -> attachmentService.upload(meetingUuid, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Meeting not found");
    }

    // if attachment not exists
    @Test
    void shouldThrowExceptionWhenAttachmentNotFound() {

        // Arrange
        UUID attachmentUuid = UUID.randomUUID();

        // Act + Assert
        assertThatThrownBy(() -> attachmentService.delete(attachmentUuid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Attachment not found");
    }

    private MeetingResponse createMeeting() {

        CreateMeetingRequest request = new CreateMeetingRequest(
                "Attachment Integration Test",
                "Testing attachment upload."
        );
        return meetingService.create(request);
    }
}

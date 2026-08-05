package com.sajee.meeting_mind_ai.integration.meeting;

import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import com.sajee.meeting_mind_ai.attachment.repository.MeetingAttachmentRepository;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import com.sajee.meeting_mind_ai.storage.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// run cmd: mvn -Dtest=MeetingServiceIntegrationTest test

@SpringBootTest
@ActiveProfiles("test")
public class MeetingServiceIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingRepository meetingRepository;

    @MockitoBean
    private FileStorageService storageService;

    @Autowired
    private MeetingAttachmentRepository attachmentRepository;

    // Create
    @Test
    void shouldCreateMeetingSuccessfully() {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Integration Test Meeting",
                "Testing meeting creation with real PostgreSQL."
        );

        // Act
        MeetingResponse response = meetingService.create(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.uuid()).isNotNull();
        assertThat(response.title())
                .isEqualTo("Integration Test Meeting");
        assertThat(response.description())
                .isEqualTo("Testing meeting creation with real PostgreSQL.");
        assertThat(response.status()).isNotNull();
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();

        assertThat(meetingRepository.findByUuid(response.uuid())).isPresent();
    }

    // Get
    @Test
    void shouldGetMeetingByUuidSuccessfully() {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Get Meeting Test",
                "Testing get meeting by UUID."
        );
        MeetingResponse created = meetingService.create(request);

        // Act
        MeetingResponse response = meetingService.getByUuid(created.uuid());

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.uuid())
                .isEqualTo(created.uuid());
        assertThat(response.title())
                .isEqualTo("Get Meeting Test");
        assertThat(response.description())
                .isEqualTo("Testing get meeting by UUID.");
        assertThat(response.status())
                .isEqualTo(MeetingStatus.UPLOADED);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    // Failure: if meeting uuid not found
    @Test
    void shouldThrowExceptionWhenMeetingDoesNotExist() {

        // Arrange
        UUID unknownUuid = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> meetingService.getByUuid(unknownUuid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Meeting not found with UUID: " + unknownUuid);
    }

    // Update
    @Test
    void shouldUpdateMeetingSuccessfully() {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Old Title",
                "Old Description"
        );
        MeetingResponse created = meetingService.create(request);

        // Act
        UpdateMeetingRequest updateRequest = new UpdateMeetingRequest(
                "New Title",
                "New Description"
        );
        MeetingResponse updated = meetingService.update(created.uuid(), updateRequest);

        // Assert
        assertThat(meetingRepository.findByUuid(created.uuid())).isPresent();
        assertThat(updated.uuid()).isNotNull();
        assertThat(updated.uuid()).
                isEqualTo(created.uuid());
        assertThat(updated.title())
                .isEqualTo("New Title");
        assertThat(updated.description())
                .isEqualTo("New Description");
    }

    // Delete
    @Test
    void shouldDeleteMeetingSuccessfully() {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Meeting To Delete",
                "This meeting should be deleted."
        );
        MeetingResponse created = meetingService.create(request);

        MeetingAttachment attachment1 = MeetingAttachment.builder()
                .originalFileName("meeting-notes1.pdf")
                .storedFileName("123123.pdf")
                .filePath("upload/meeting/123123.pdf")
                .fileType("application/pdf")
                .fileExtension("pdf")
                .fileSize(1024L)
                .checksum("checksum-123")
                .build();

        MeetingAttachment attachment2 = MeetingAttachment.builder()
                .originalFileName("meeting-notes2.txt")
                .storedFileName("12341234.pdf")
                .filePath("upload/meeting/12341234.txt")
                .fileType("text/plain")
                .fileExtension("txt")
                .fileSize(2048L)
                .checksum("checksum-456")
                .build();

        attachment1.setMeeting(
                meetingRepository.findByUuid(created.uuid()).orElseThrow()
        );
        attachment2.setMeeting(
                meetingRepository.findByUuid(created.uuid()).orElseThrow()
        );

        attachmentRepository.saveAll(List.of(attachment1, attachment2));

        // Verify attachment records exists with meeting
        assertThat(attachmentRepository.findByMeetingUuid(created.uuid())).hasSize(2);

        // Act
        meetingService.delete(created.uuid());

        // Assert - meeting is deleted
        assertThat(meetingRepository.findByUuid(created.uuid())).isEmpty();

        // Assert - attachment records are deleted
        assertThat(attachmentRepository.findByMeetingUuid(created.uuid())).isEmpty();

        // Assert - physical files were requested for deletion
        Mockito.verify(storageService)
                .delete("upload/meeting/123123.pdf");

        Mockito.verify(storageService)
                .delete("upload/meeting/12341234.txt");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingMeeting() {

        // Arrange
        UUID unknownUuid = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> meetingService.delete(unknownUuid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Meeting not found with UUID: " + unknownUuid);
    }
}

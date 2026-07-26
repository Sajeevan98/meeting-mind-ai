package com.sajee.meeting_mind_ai.meeting.service;

import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import com.sajee.meeting_mind_ai.meeting.mapper.MeetingMapper;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceImplTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MeetingMapper meetingMapper;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @Test
    void shouldCreateMeetingSuccessfully() {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest("Sprint Planning");

        Meeting meeting = Meeting.builder()
                .title("Sprint Planning")
                .build();

        Meeting savedMeeting = Meeting.builder()
                .title("Sprint Planning")
                .build();

        MeetingResponse response =
                new MeetingResponse(
                        savedMeeting.getUuid(),
                        savedMeeting.getTitle(),
                        savedMeeting.getStatus(),
                        0,
                        0,
                        savedMeeting.getCreatedAt(),
                        savedMeeting.getUpdatedAt()
                );

        when(meetingMapper.toEntity(request))
                .thenReturn(meeting);

        when(meetingRepository.save(any(Meeting.class)))
                .thenReturn(savedMeeting);

        when(meetingMapper.toResponse(savedMeeting))
                .thenReturn(response);


        // Act
        MeetingResponse result = meetingService.create(request);

        // Assert
        assertThat(result).isNotNull();

        assertThat(result.title())
                .isEqualTo("Sprint Planning");

        assertThat(result.status())
                .isEqualTo(MeetingStatus.UPLOADED);

        verify(meetingMapper)
                .toEntity(request);

        verify(meetingRepository)
                .save(any(Meeting.class));

        verify(meetingMapper)
                .toResponse(savedMeeting);
    }

    // Success / Found
    @Test
    void shouldReturnMeetingWhenUuidExists() {

        UUID uuid = UUID.randomUUID();

        Meeting meeting = Meeting.builder()
                .title("Sprint Planning")
                .build();

        MeetingResponse response =
                new MeetingResponse(
                        uuid,
                        "Sprint Planning",
                        MeetingStatus.UPLOADED,
                        0,
                        0,
                        null,
                        null
                );

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.of(meeting));

        when(meetingMapper.toResponse(meeting))
                .thenReturn(response);

        MeetingResponse result = meetingService.getByUuid(uuid);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Sprint Planning");

        verify(meetingRepository).findByUuid(uuid);
        verify(meetingMapper).toResponse(meeting);
    }

    // Not Found
    @Test
    void shouldThrowExceptionWhenMeetingDoesNotExist() {

        UUID uuid = UUID.randomUUID();

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> meetingService.getByUuid(uuid)
        );

        verify(meetingRepository).findByUuid(uuid);
        verify(meetingMapper, never()).toResponse((Meeting) any());
    }

    @Test
    void shouldUpdateMeetingSuccessfully() {

        UUID uuid = UUID.randomUUID();

        UpdateMeetingRequest request = new UpdateMeetingRequest("Updated Sprint Planning");

        Meeting meeting = Meeting.builder()
                .title("Old Title")
                .build();

        MeetingResponse response =
                new MeetingResponse(
                        meeting.getUuid(),
                        "Updated Sprint Planning",
                        MeetingStatus.UPLOADED,
                        0,
                        0,
                        meeting.getCreatedAt(),
                        meeting.getUpdatedAt()
                );

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.of(meeting));

        when(meetingRepository.save(meeting))
                .thenReturn(meeting);

        when(meetingMapper.toResponse(meeting))
                .thenReturn(response);

        MeetingResponse result = meetingService.update(uuid, request);

        assertThat(result.title())
                .isEqualTo("Updated Sprint Planning");

        verify(meetingRepository).findByUuid(uuid);
        verify(meetingRepository).save(meeting);
        verify(meetingMapper).toResponse(meeting);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUnknownMeeting() {

        UUID uuid = UUID.randomUUID();

        UpdateMeetingRequest request = new UpdateMeetingRequest("Updated");

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> meetingService.update(uuid, request)
        );

        verify(meetingRepository).findByUuid(uuid);

        verify(meetingRepository, never())
                .save(any());

        verify(meetingMapper, never())
                .toResponse((Meeting) any());
    }

    @Test
    void shouldReturnMeetingsSuccessfully() {

        // Arrange
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Meeting meeting = Meeting.builder()
                .title("Sprint Planning")
                .build();

        MeetingResponse response = new MeetingResponse(
                meeting.getUuid(),
                meeting.getTitle(),
                meeting.getStatus(),
                0,
                0,
                meeting.getCreatedAt(),
                meeting.getUpdatedAt()
        );

        Page<Meeting> meetingPage =
                new PageImpl<>(List.of(meeting));

        when(meetingRepository.findAll(pageable))
                .thenReturn(meetingPage);

        when(meetingMapper.toResponse(meeting))
                .thenReturn(response);

        // Act
        Page<MeetingResponse> result =
                meetingService.getAll(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().title())
                .isEqualTo("Sprint Planning");

        verify(meetingRepository).findAll(pageable);
        verify(meetingMapper).toResponse(meeting);
    }

    @Test
    void shouldReturnEmptyPageWhenNoMeetingsExist() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        when(meetingRepository.findAll(pageable))
                .thenReturn(Page.empty());

        // Act
        Page<MeetingResponse> result =
                meetingService.getAll(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(meetingRepository).findAll(pageable);

        verify(meetingMapper, never())
                .toResponse((Meeting) any());
    }

    @Test
    void shouldDeleteMeetingSuccessfully() {

        // Arrange
        UUID uuid = UUID.randomUUID();

        Meeting meeting = Meeting.builder()
                .title("Sprint Planning")
                .build();

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.of(meeting));

        // Act
        meetingService.delete(uuid);

        // Assert
        verify(meetingRepository).findByUuid(uuid);
        verify(meetingRepository).delete(meeting);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingMeeting() {

        // Arrange
        UUID uuid = UUID.randomUUID();

        when(meetingRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> meetingService.delete(uuid))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Meeting not found with UUID: " + uuid);

        verify(meetingRepository).findByUuid(uuid);

        verify(meetingRepository, never())
                .delete(any(Meeting.class));
    }
}

package com.sajee.meeting_mind_ai.meeting.repository;

import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    void shouldSaveMeetingSuccessfully() {

        // Act
        Meeting savedMeeting = createMeeting("Sprint Planning Meeting");

        // Assert
        assertThat(savedMeeting).isNotNull();

        assertThat(savedMeeting.getId()).isNotNull();

        assertThat(savedMeeting.getUuid()).isNotNull();

        assertThat(savedMeeting.getTitle())
                .isEqualTo("Sprint Planning Meeting");

        assertThat(savedMeeting.getStatus())
                .isEqualTo(MeetingStatus.UPLOADED);

        assertThat(savedMeeting.getCreatedAt()).isNotNull();

        assertThat(savedMeeting.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindMeetingByUuid() {

        // Arrange
        Meeting savedMeeting = createMeeting("Sprint Planning Meeting");

        // Act
        Optional<Meeting> result = meetingRepository.findByUuid(savedMeeting.getUuid());

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getUuid())
                .isEqualTo(savedMeeting.getUuid());

        assertThat(result.get().getTitle())
                .isEqualTo("Sprint Planning Meeting");
    }

    @Test
    void shouldReturnEmptyWhenUuidDoesNotExist() {

        // Act
        Optional<Meeting> result = meetingRepository.findByUuid(UUID.randomUUID());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenMeetingExistsByUuid() {

        // Arrange
        Meeting savedMeeting = createMeeting("Sprint Planning Meeting");

        // Act
        boolean exists = meetingRepository.existsByUuid(savedMeeting.getUuid());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenMeetingDoesNotExistByUuid() {

        // Act
        boolean exists = meetingRepository.existsByUuid(UUID.randomUUID());

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnMeetingsOrderedByCreatedAtDesc() throws InterruptedException {

        // Arrange
        Meeting first = createMeeting("Meeting A");

        // If all three entities are created in the same millisecond,
        // they could receive identical timestamps, making the ordering unpredictable.
        // A tiny delay guarantees different timestamps.
        Thread.sleep(5);

        Meeting second = createMeeting("Meeting B");

        Thread.sleep(5);

        Meeting third = createMeeting("Meeting C");

        // Act
        List<Meeting> meetings = meetingRepository.findAllByOrderByCreatedAtDesc();

        // Assert
        assertThat(meetings)
                .hasSize(3);

        assertThat(meetings.get(0).getTitle())
                .isEqualTo("Meeting C");

        assertThat(meetings.get(1).getTitle())
                .isEqualTo("Meeting B");

        assertThat(meetings.get(2).getTitle())
                .isEqualTo("Meeting A");
    }

    // Helper Method
    private Meeting createMeeting(String title) {

        return meetingRepository.save(
                Meeting.builder()
                        .title(title)
                        .build()
        );
    }
}

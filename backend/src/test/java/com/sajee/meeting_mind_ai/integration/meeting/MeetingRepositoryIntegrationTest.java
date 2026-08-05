package com.sajee.meeting_mind_ai.integration.meeting;

import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// run cmd: mvn -Dtest=MeetingRepositoryIntegrationTest test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    void shouldSaveAndFindMeetingByUuid() {

        // Arrange
        Meeting meeting = Meeting.builder()
                .title("Integration Test Meeting")
                .description("Testing with real PostgreSQL")
                .status(MeetingStatus.UPLOADED)
                .build();

        // Act
        Meeting savedMeeting = meetingRepository.save(meeting);

        Optional<Meeting> result = meetingRepository.findByUuid(savedMeeting.getUuid());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUuid())
                .isEqualTo(savedMeeting.getUuid());
        assertThat(result.get().getTitle())
                .isEqualTo("Integration Test Meeting");
        assertThat(result.get().getDescription())
                .isEqualTo("Testing with real PostgreSQL");
        assertThat(result.get().getStatus())
                .isEqualTo(MeetingStatus.UPLOADED);
    }

}

package com.sajee.meeting_mind_ai.integration.meeting;

import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// run cmd: mvn -Dtest=MeetingControllerIntegrationTest test

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MeetingControllerIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingService meetingService;

    @Test
    void shouldCreateMeetingSuccessfully() throws Exception {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Controller Integration Test",
                "Testing meeting creation through HTTP API."
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/meetings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.uuid").exists())
                .andExpect(jsonPath("$.data.title")
                        .value("Controller Integration Test"))
                .andExpect(jsonPath("$.data.description")
                        .value("Testing meeting creation through HTTP API."))
                .andExpect(jsonPath("$.data.status")
                        .value("UPLOADED"));
    }

    @Test
    void shouldGetMeetingByUuidSuccessfully() throws Exception {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Get Meeting Test",
                "Testing get meeting by UUID."
        );
        MeetingResponse created = meetingService.create(request);

        // Act & Assert
        mockMvc.perform(
                        get("/api/v1/meetings/{uuid}", created.uuid())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.uuid").value(created.uuid().toString()))
                .andExpect(jsonPath("$.data.title").value("Get Meeting Test"))
                .andExpect(jsonPath("$.data.description")
                        .value("Testing get meeting by UUID."))
                .andExpect(jsonPath("$.data.status").value("UPLOADED"))
                .andExpect(jsonPath("$.data.attachmentCount").value(0))
                .andExpect(jsonPath("$.data.analysisCount").value(0));
    }

    @Test
    void shouldReturn404WhenMeetingNotFound() throws Exception {

        // Arrange
        UUID randomUuid = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(
                        get("/api/v1/meetings/{uuid}", randomUuid)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Meeting not found with UUID: " + randomUuid));
    }

    @Test
    void shouldUpdateMeetingSuccessfully() throws Exception {

        // Arrange
        CreateMeetingRequest createRequest = new CreateMeetingRequest(
                "Old Meeting Title",
                "Old Meeting Description"
        );
        MeetingResponse created = meetingService.create(createRequest);

        UpdateMeetingRequest updateRequest = new UpdateMeetingRequest(
                "Updated Meeting Title",
                "Updated Meeting Description"
        );

        // Act & Assert
        mockMvc.perform(
                        put("/api/v1/meetings/{uuid}", created.uuid())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.uuid")
                        .value(created.uuid().toString()))
                .andExpect(jsonPath("$.data.title")
                        .value("Updated Meeting Title"))
                .andExpect(jsonPath("$.data.description")
                        .value("Updated Meeting Description"));
    }

    @Test
    void shouldDeleteMeetingSuccessfully() throws Exception {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest(
                "Meeting To Delete",
                "This meeting should be deleted."
        );
        MeetingResponse created = meetingService.create(request);

        // Act
        mockMvc.perform(
                        delete("/api/v1/meetings/{uuid}", created.uuid())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Assert
        mockMvc.perform(
                        get("/api/v1/meetings/{uuid}", created.uuid())
                )
                .andExpect(status().isNotFound());
    }

    // Invalid create request
    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        // Arrange
        CreateMeetingRequest invalidRequest = new CreateMeetingRequest(
                "",
                ""
        );

        // Act & Assert
        mockMvc.perform(
                        post("/api/v1/meetings")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // Invalid update request
    @Test
    void shouldRejectInvalidUpdateRequest() throws Exception {

        // Arrange
        CreateMeetingRequest createRequest = new CreateMeetingRequest(
                "Valid Meeting",
                "Valid Description"
        );
        MeetingResponse created = meetingService.create(createRequest);

        UpdateMeetingRequest invalidRequest = new UpdateMeetingRequest(
                "",
                ""
        );

        // Act & Assert
        mockMvc.perform(
                        put("/api/v1/meetings/{uuid}", created.uuid())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

}

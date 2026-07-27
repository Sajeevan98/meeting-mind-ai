package com.sajee.meeting_mind_ai.meeting.controller;

import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(MeetingController.class)
public class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MeetingService meetingService;

    @Test
    void shouldCreateMeetingSuccessfully() throws Exception {

        UUID uuid = UUID.randomUUID();
        CreateMeetingRequest request = new CreateMeetingRequest("Sprint Planning");

        MeetingResponse response =
                new MeetingResponse(
                        uuid,
                        "Sprint Planning",
                        MeetingStatus.UPLOADED,
                        1,
                        5,
                        Instant.now(),
                        Instant.now()
                );

        when(meetingService.create(any(CreateMeetingRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Meeting created successfully."))
                .andExpect(jsonPath("$.data.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.data.attachmentCount").value(1))
                .andExpect(jsonPath("$.data.title").value("Sprint Planning"))
                .andExpect(jsonPath("$.data.status").value("UPLOADED"));

        verify(meetingService)
                .create(any(CreateMeetingRequest.class));
    }

    // Invalid Request (400 Bad Request)
    @Test
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {

        // Arrange
        CreateMeetingRequest request = new CreateMeetingRequest("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(meetingService, never())
                .create(any());
    }

    @Test
    void shouldGetMeetingSuccessfully() throws Exception {

        // Arrange
        UUID uuid = UUID.randomUUID();

        MeetingResponse response = new MeetingResponse(
                uuid,
                "Sprint Planning",
                MeetingStatus.UPLOADED,
                0,
                0,
                Instant.now(),
                Instant.now()
        );

        when(meetingService.getByUuid(uuid))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/meeting/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Meeting retrieved successfully."))
                .andExpect(jsonPath("$.data.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.data.title").value("Sprint Planning"))
                .andExpect(jsonPath("$.data.status").value("UPLOADED"));

        verify(meetingService).getByUuid(uuid);
    }

    @Test
    void shouldGetMeetingsSuccessfully() throws Exception {

        // Arrange
        MeetingResponse response = new MeetingResponse(
                UUID.randomUUID(),
                "Sprint Planning",
                MeetingStatus.UPLOADED,
                0,
                0,
                Instant.now(),
                Instant.now()
        );

        Page<MeetingResponse> page =
                new PageImpl<>(List.of(response));

        when(meetingService.getAll(any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/meeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title")
                        .value("Sprint Planning"))
                .andExpect(jsonPath("$.data.content.length()")
                        .value(1));

        verify(meetingService)
                .getAll(any(Pageable.class));
    }

    @Test
    void shouldUpdateMeetingSuccessfully() throws Exception {

        UUID uuid = UUID.randomUUID();

        UpdateMeetingRequest request = new UpdateMeetingRequest("Updated Meeting");

        MeetingResponse response =
                new MeetingResponse(
                        uuid,
                        "Updated Meeting",
                        MeetingStatus.UPLOADED,
                        0,
                        0,
                        Instant.now(),
                        Instant.now()
                );

        when(meetingService.update(eq(uuid), any(UpdateMeetingRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/meeting/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title")
                        .value("Updated Meeting"));

        verify(meetingService)
                .update(eq(uuid), any(UpdateMeetingRequest.class));
    }

    @Test
    void shouldDeleteMeetingSuccessfully() throws Exception {

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/meeting/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Meeting deleted successfully."));

        verify(meetingService).delete(uuid);
    }

    @Test
    void shouldReturnNotFoundWhenMeetingDoesNotExist() throws Exception {

        UUID uuid = UUID.randomUUID();

        when(meetingService.getByUuid(uuid))
                .thenThrow(new ResourceNotFoundException("Meeting not found with UUID: " + uuid));

        mockMvc.perform(get("/api/v1/meeting/{uuid}", uuid))
                .andExpect(status().isNotFound());

        verify(meetingService).getByUuid(uuid);
    }
}

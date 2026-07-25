package com.sajee.meeting_mind_ai.meeting.controller;

import com.sajee.meeting_mind_ai.common.response.ApiResponse;
import com.sajee.meeting_mind_ai.common.util.ApiEndpoints;
import com.sajee.meeting_mind_ai.common.util.ApiMessages;
import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.request.UpdateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiEndpoints.MEETINGS)
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<ApiResponse<MeetingResponse>> createMeeting(
            @Valid @RequestBody CreateMeetingRequest request
    ) {

        MeetingResponse response = meetingService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(ApiMessages.MEETING_CREATED, response));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<MeetingResponse>> getMeeting(@PathVariable UUID uuid) {

        return ResponseEntity.ok(
                ApiResponse.success(ApiMessages.MEETING_RETRIEVED, meetingService.getByUuid(uuid))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MeetingResponse>>> getMeetings() {

        return ResponseEntity.ok(
                ApiResponse.success(ApiMessages.MEETINGS_RETRIEVED, meetingService.getAll())
        );
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<MeetingResponse>> updateMeeting(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateMeetingRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(ApiMessages.MEETING_UPDATED, meetingService.update(uuid, request))
        );
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteMeeting(@PathVariable UUID uuid) {

        meetingService.delete(uuid);

        return ResponseEntity.ok(
                ApiResponse.success(ApiMessages.MEETING_DELETED)
        );
    }
}

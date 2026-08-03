package com.sajee.meeting_mind_ai.attachment.controller;

import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import com.sajee.meeting_mind_ai.attachment.service.MeetingAttachmentService;
import com.sajee.meeting_mind_ai.common.response.ApiResponse;
import com.sajee.meeting_mind_ai.common.util.ApiEndpoints;
import com.sajee.meeting_mind_ai.common.util.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiEndpoints.ATTACHMENTS)
@RequiredArgsConstructor
@Slf4j
public class MeetingAttachmentController {

    private final MeetingAttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AttachmentResponse>> uploadAttachment(
            @RequestParam("uuid") UUID meetingUuid,
            @RequestParam("file") MultipartFile file
    ) {

        log.info("Uploading attachment for meeting: {}", meetingUuid);

        AttachmentResponse response = attachmentService.upload(meetingUuid, file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ApiMessages.ATTACHMENT_UPLOADED, response));
    }

    @GetMapping("/meeting/{meetingUuid}")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getAttachments(
            @PathVariable UUID meetingUuid
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.ATTACHMENTS_RETRIEVED,
                        attachmentService.getByMeeting(meetingUuid)
                )
        );
    }

    @DeleteMapping("/{attachmentUuid}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(
            @PathVariable UUID attachmentUuid
    ) {

        attachmentService.delete(attachmentUuid);

        return ResponseEntity.ok(
                ApiResponse.success(ApiMessages.ATTACHMENT_DELETED)
        );
    }
}

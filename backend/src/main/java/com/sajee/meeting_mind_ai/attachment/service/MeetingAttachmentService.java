package com.sajee.meeting_mind_ai.attachment;

import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MeetingAttachmentService {

    AttachmentResponse upload(UUID meetingUuid, MultipartFile file);

    List<AttachmentResponse> getByMeeting(UUID meetingUuid);

    void delete(UUID attachmentUuid);
}

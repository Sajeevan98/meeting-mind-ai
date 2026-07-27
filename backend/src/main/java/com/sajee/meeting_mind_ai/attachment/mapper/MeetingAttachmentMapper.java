package com.sajee.meeting_mind_ai.attachment.mapper;

import com.sajee.meeting_mind_ai.attachment.dto.AttachmentResponse;
import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MeetingAttachmentMapper {

    AttachmentResponse toResponse(MeetingAttachment attachment);

    List<AttachmentResponse> toResponse(List<MeetingAttachment> attachments);
}
package com.sajee.meeting_mind_ai.meeting.mapper;

import com.sajee.meeting_mind_ai.meeting.dto.request.CreateMeetingRequest;
import com.sajee.meeting_mind_ai.meeting.dto.response.MeetingResponse;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MeetingMapper {

    Meeting toEntity(CreateMeetingRequest request);

    @Mapping(
            target = "attachmentCount",
            expression = "java(meeting.getAttachments().size())"
    )
    @Mapping(
            target = "analysisCount",
            expression = "java(meeting.getAnalyses().size())"
    )
    MeetingResponse toResponse(Meeting meeting);

    // Keep all mapping centralized here
    List<MeetingResponse> toResponse(List<Meeting> meetings);
}

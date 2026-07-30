package com.sajee.meeting_mind_ai.analysis.mapper;

import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.entity.MeetingAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MeetingAnalysisMapper {

    MeetingAnalysisResponse toResponse(MeetingAnalysis analysis);

    List<MeetingAnalysisResponse> toResponse(List<MeetingAnalysis> analyses);
}
package com.sajee.meeting_mind_ai.analysis.repository;

import com.sajee.meeting_mind_ai.analysis.entity.MeetingAnalysis;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingAnalysisRepository extends JpaRepository<MeetingAnalysis, Long> {


    Optional<MeetingAnalysis> findByUuid(UUID uuid);

    List<MeetingAnalysis> findAllByMeetingUuidOrderByAnalysisVersionDesc(UUID meetingUuid);

    Optional<MeetingAnalysis> findTopByMeetingOrderByAnalysisVersionDesc(Meeting meeting);

}

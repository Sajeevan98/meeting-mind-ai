package com.sajee.meeting_mind_ai.meeting.repository;

import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {


    Optional<Meeting> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    List<Meeting> findAllByOrderByCreatedAtDesc();
}

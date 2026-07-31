package com.sajee.meeting_mind_ai.attachment.repository;

import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingAttachmentRepository extends JpaRepository<MeetingAttachment, Long> {

    Optional<MeetingAttachment> findByUuid(UUID uuid);

    List<MeetingAttachment> findAllByMeetingUuidOrderByCreatedAtDesc(UUID meetingUuid);

    boolean existsByChecksum(String checksum);

    List<MeetingAttachment> findByMeetingUuid(UUID uuid);
}

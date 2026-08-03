package com.sajee.meeting_mind_ai.meeting.entity;

import com.sajee.meeting_mind_ai.analysis.entity.MeetingAnalysis;
import com.sajee.meeting_mind_ai.common.util.AuditableEntity;
import com.sajee.meeting_mind_ai.meeting.enums.MeetingStatus;
import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "meetings",
        indexes = @Index(name = "idx_meeting_uuid", columnList = "uuid")
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Setter
    @Column(nullable = false, length = 255)
    private String title;

    @Setter
    @Column(nullable = false, length = 1000)
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MeetingStatus status = MeetingStatus.UPLOADED;

    @Builder.Default
    @OneToMany(
            mappedBy = "meeting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<MeetingAttachment> attachments = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "meeting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<MeetingAnalysis> analyses = new ArrayList<>();

    // -------------------------------------------------------------------
    // Relationship Helper Methods
    // -------------------------------------------------------------------

    public void addAttachment(MeetingAttachment attachment) {
        attachments.add(attachment);
        attachment.setMeeting(this);
    }

    public void removeAttachment(MeetingAttachment attachment) {
        attachments.remove(attachment);
        attachment.setMeeting(null);
    }

    public void addAnalysis(MeetingAnalysis analysis) {
        analyses.add(analysis);
        analysis.setMeeting(this);
    }

    public void removeAnalysis(MeetingAnalysis analysis) {
        analyses.remove(analysis);
        analysis.setMeeting(null);
    }

    // --------------------------------------------------------------
    // Domain Methods
    // --------------------------------------------------------------

    public void markProcessing() {
        this.status = MeetingStatus.PROCESSING;
    }

    public void markCompleted() {
        this.status = MeetingStatus.COMPLETED;
    }

    public void markFailed() {
        this.status = MeetingStatus.FAILED;
    }

    public boolean hasAttachments() {
        return !attachments.isEmpty();
    }

    public boolean hasAnalyses() {
        return !analyses.isEmpty();
    }
}
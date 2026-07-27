package com.sajee.meeting_mind_ai.attachment.entity;

import com.sajee.meeting_mind_ai.common.util.AuditableEntity;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "meeting_attachments",
        indexes = @Index(name = "idx_attachment_meeting", columnList = "meeting_id")
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttachment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    @Setter
    private Meeting meeting;

    @Setter
    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Setter
    @Column(name = "stored_file_name", nullable = false, length = 255)
    private String storedFileName;

    @Setter
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Setter
    @Column(name = "file_type", nullable = false, length = 255)
    private String fileType;

    // Avoids repeatedly parsing the filename to determine the extractor
    @Setter
    @Column(name = "file_extension", nullable = false, length = 20)
    private String fileExtension;

    @Setter
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    // Detects duplicate uploads and verifies file integrity
    @Setter
    @Column(name = "checksum", nullable = false, length = 64)
    private String checksum;
}
package com.sajee.meeting_mind_ai.analysis.entity;

import com.sajee.meeting_mind_ai.analysis.enums.AIProvider;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.model.ActionItem;
import com.sajee.meeting_mind_ai.common.util.AuditableEntity;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "meeting_analyses",
        indexes = {
                @Index(name = "idx_analysis_uuid", columnList = "uuid"),
                @Index(name = "idx_analysis_meeting", columnList = "meeting_id")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAnalysis extends AuditableEntity {

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
    @Column(columnDefinition = "TEXT", nullable = false)
    private String summary;

    @Setter
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<ActionItem> actionItems;

    @Setter
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<String> decisions;

    @Setter
    @Column(name = "raw_ai_response", columnDefinition = "TEXT")
    private String rawAiResponse;

    // Distinguishes OpenAI, Gemini, Claude, etc.
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AIProvider provider;

    // Stores the exact model (such as gpt-5, gemini-2.5-pro)
    @Setter
    @Column(nullable = false, length = 100)
    private String model;

    // Supports re-analysis and version history
    @Setter
    @Column(nullable = false)
    private Integer analysisVersion;

    // Tracks which prompt template produced the result
    @Setter
    @Column(nullable = false)
    private Integer promptVersion;

    // Useful for monitoring, dashboards, and performance comparisons
    @Setter
    @Column(nullable = false)
    private Long processingTimeMs;

    // Keeps the analysis lifecycle independent of the meeting lifecycle
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnalysisStatus status;
}

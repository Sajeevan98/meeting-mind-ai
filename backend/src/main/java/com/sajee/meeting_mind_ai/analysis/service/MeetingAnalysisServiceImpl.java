package com.sajee.meeting_mind_ai.analysis.service;

import com.sajee.meeting_mind_ai.ai.dto.request.AiAnalyzeRequest;
import com.sajee.meeting_mind_ai.ai.dto.response.AiAnalysisResult;
import com.sajee.meeting_mind_ai.ai.service.AiService;
import com.sajee.meeting_mind_ai.analysis.dto.MeetingAnalysisResponse;
import com.sajee.meeting_mind_ai.analysis.entity.MeetingAnalysis;
import com.sajee.meeting_mind_ai.analysis.enums.AnalysisStatus;
import com.sajee.meeting_mind_ai.analysis.mapper.MeetingAnalysisMapper;
import com.sajee.meeting_mind_ai.analysis.repository.MeetingAnalysisRepository;
import com.sajee.meeting_mind_ai.attachment.entity.MeetingAttachment;
import com.sajee.meeting_mind_ai.common.exception.ResourceNotFoundException;
import com.sajee.meeting_mind_ai.common.exception.business.MeetingAnalysisException;
import com.sajee.meeting_mind_ai.document.dto.ProcessedDocument;
import com.sajee.meeting_mind_ai.document.service.DocumentProcessingService;
import com.sajee.meeting_mind_ai.meeting.entity.Meeting;
import com.sajee.meeting_mind_ai.meeting.repository.MeetingRepository;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingAnalysisServiceImpl implements MeetingAnalysisService {

    private final MeetingRepository meetingRepository;
    private final MeetingAnalysisRepository analysisRepository;

    private final DocumentProcessingService documentProcessingService;
    private final AiService aiService;

    private final MeetingAnalysisMapper meetingAnalysisMapper;

    @Override
    public MeetingAnalysisResponse analyze(UUID meetingUuid, AiAnalyzeRequest request) {

        log.info("Starting meeting analysis. Meeting: {}", meetingUuid);

        Meeting meeting = meetingRepository.findByUuid(meetingUuid)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Meeting not found with UUID: " + meetingUuid));

        if (meeting.getAttachments().isEmpty()) {

            log.warn("Meeting {} has no attachments.", meetingUuid);
            throw new ResourceNotFoundException("No attachments found for meeting: " + meetingUuid);
        }

        try {

            StringBuilder combinedText = new StringBuilder();

            log.info("Processed documents Count: {}", meeting.getAttachments().size());
            for (MeetingAttachment attachment : meeting.getAttachments()) {

                log.info("Processing attachment: {}", attachment.getOriginalFileName());

                StoredFile storedFile = new StoredFile(
                        attachment.getOriginalFileName(),
                        attachment.getStoredFileName(),
                        attachment.getFilePath(),
                        attachment.getFileType(),
                        attachment.getFileExtension(),
                        attachment.getFileSize(),
                        attachment.getChecksum()
                );

                // Extract text
                ProcessedDocument document =
                        documentProcessingService.process(storedFile);

                combinedText.append(System.lineSeparator())
                        .append("===================================================")
                        .append(System.lineSeparator())
                        .append("FILE : ")
                        .append(attachment.getOriginalFileName())
                        .append(System.lineSeparator())
                        .append("===================================================")
                        .append(System.lineSeparator())
                        .append(document.extractedText())
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }

            ProcessedDocument mergedDocument = new ProcessedDocument(
                    combinedText.toString(),
                    combinedText.length(),
                    combinedText.toString().isBlank()
                            ? 0
                            : combinedText.toString().trim().split("\\s+").length
            );

            // Contact AI service
            AiAnalysisResult result =
                    aiService.analyzeMeeting(mergedDocument, request);

            Integer nextVersion =
                    analysisRepository
                            .findTopByMeetingOrderByAnalysisVersionDesc(meeting)
                            .map(analysis -> analysis.getAnalysisVersion() + 1)
                            .orElse(1);

            MeetingAnalysis analysis = MeetingAnalysis.builder()
                    .meeting(meeting)
                    .summary(result.summary())
                    .actionItems(result.actionItems())
                    .decisions(result.decisions())
                    .risks(result.risks())
                    .nextSteps(result.nextSteps())
                    .rawAiResponse(null)
                    .provider(result.provider())
                    .model(result.model())
                    .analysisVersion(nextVersion)
                    .promptVersion(result.promptVersion())
                    .processingTimeMs(result.processingTimeMs())
                    .status(AnalysisStatus.COMPLETED)
                    .build();

            // Save data
            MeetingAnalysis savedAnalysis =
                    analysisRepository.save(analysis);

            log.info("Meeting analyzed successfully. Meeting: {}, Analysis Version: {}", meetingUuid, nextVersion);

            return meetingAnalysisMapper.toResponse(savedAnalysis);

        } catch (Exception ex) {

            log.error("Meeting analysis failed. Meeting UUID: {}", meetingUuid, ex);

            throw new MeetingAnalysisException("Failed to analyze meeting.", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingAnalysisResponse> getAnalyses(UUID meetingUuid) {

        log.info("Fetching analyses for meeting: {}", meetingUuid);

        if (!meetingRepository.existsByUuid(meetingUuid)) {

            log.warn("Meeting not found: {}", meetingUuid);
            throw new ResourceNotFoundException("Meeting not found with UUID: " + meetingUuid);
        }

        List<MeetingAnalysis> analyses =
                analysisRepository.findAllByMeetingUuidOrderByAnalysisVersionDesc(meetingUuid);

        log.info("Found {} analysis version(s) for meeting: {}", analyses.size(), meetingUuid);

        return meetingAnalysisMapper.toResponse(analyses);

    }

    @Override
    @Transactional(readOnly = true)
    public MeetingAnalysisResponse get(UUID analysisUuid) {

        log.info("Fetching analysis: {}", analysisUuid);

        MeetingAnalysis analysis =
                analysisRepository.findByUuid(analysisUuid)
                        .orElseThrow(() -> {

                            log.warn("Analysis not found: {}", analysisUuid);
                            return new ResourceNotFoundException("Analysis not found with UUID: " + analysisUuid);
                        });

        log.info("Analysis fetched successfully. Version: {}", analysis.getAnalysisVersion());

        return meetingAnalysisMapper.toResponse(analysis);
    }

    @Override
    public void delete(UUID analysisUuid) {

        log.info("Deleting analysis: {}", analysisUuid);

        MeetingAnalysis analysis = analysisRepository.findByUuid(analysisUuid)
                .orElseThrow(() -> {

                    log.warn("Analysis not found: {}", analysisUuid);
                    return new ResourceNotFoundException("Analysis not found with UUID: " + analysisUuid);
                });

        analysisRepository.delete(analysis);

        log.info(
                "Analysis deleted successfully. UUID: {}, Version: {}",
                analysisUuid,
                analysis.getAnalysisVersion()
        );
    }
}

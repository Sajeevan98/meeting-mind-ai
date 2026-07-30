package com.sajee.meeting_mind_ai.ai.prompt;

public final class MeetingAnalysisPrompt {

    private MeetingAnalysisPrompt() {
    }

    public static final Integer VERSION = 1;

    public static final String ANALYSIS = """
            You are an AI meeting assistant.
            Analyze the meeting notes below.
            Return ONLY valid JSON.
            Do not include markdown.
            Do not include explanation.
            Do not wrap the JSON inside ```.
            Return exactly this structure:
            
            {
                "summary": "...",
                "actionItems": [
                    {
                        "assignee": "...",
                        "task": "...",
                        "deadline": "..."
                    }
                ],
                "decisions": [
                    "..."
                ],
                "risks": [
                    "..."
                ],
                "nextSteps": [
                    "..."
                ]
            }
            
            Meeting Notes:
            
            %s
            """;
}
import jsPDF from 'jspdf'

export const downloadAsPdf = (analysis) => {

    const pdf = new jsPDF();

    const pageWidth = pdf.internal.pageSize.getWidth();

    const margin = 20;

    const contentWidth = pageWidth - margin * 2;

    let y = 20;

    const addText = (text, options = {}) => {

        const { fontSize = 11, fontStyle = "normal", spacing = 6 } = options;

        pdf.setFontSize(fontSize);
        pdf.setFont("helvetica", fontStyle);

        const lines = pdf.splitTextToSize(String(text ?? ""), contentWidth);

        const lineHeight = fontSize * 0.5;

        if (y + lines.length * lineHeight > 280) {

            pdf.addPage();

            y = 20;
        }

        pdf.text(lines, margin, y);

        y += lines.length * lineHeight + spacing;
    };

    // title
    addText("Meeting Analysis", {

        fontSize: 18,
        fontStyle: "bold",
        spacing: 10,
    });

    // metadata
    addText(`Provider: ${analysis.provider}`);

    addText(`Model: ${analysis.model}`);

    addText(`Status: ${analysis.status}`);

    addText(`Analysis Version: ${analysis.analysisVersion}`);

    addText(`Created: ${analysis.createdAt}`);

    // summary
    addText("Summary", {
        fontSize: 14,
        fontStyle: "bold",
        spacing: 5,
    });

    addText(analysis.summary || "No summary available.");

    // action-items
    if (analysis.actionItems?.length > 0) {
        addText("Action Items", {
            fontSize: 14,
            fontStyle: "bold",
            spacing: 5,
        });

        analysis.actionItems.forEach((item, index) => {
            addText(`${index + 1}. ${item.assignee}: ${item.task}`);

            addText(`   Deadline: ${item.deadline}`);
        });
    }

    // decisions
    if (analysis.decisions?.length > 0) {
        addText("Decisions", {
            fontSize: 14,
            fontStyle: "bold",
            spacing: 5,
        });

        analysis.decisions.forEach((decision, index) => {
            addText(`${index + 1}. ${decision}`);
        });
    }

    // Risks
    if (analysis.risks?.length > 0) {
        addText("Risks", {
            fontSize: 14,
            fontStyle: "bold",
            spacing: 5,
        });

        analysis.risks.forEach((risk, index) => {
            addText(`${index + 1}. ${risk}`);
        });
    }

    // Next Steps
    if (analysis.nextSteps?.length > 0) {
        addText("Next Steps", {
            fontSize: 14,
            fontStyle: "bold",
            spacing: 5,
        });

        analysis.nextSteps.forEach((step, index) => {
            addText(`${index + 1}. ${step}`);
        });
    }

    pdf.save(`meeting-analysis-${analysis.uuid}.pdf`);
}

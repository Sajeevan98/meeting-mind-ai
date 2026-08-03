import { Button, Card, CardContent, Divider, List, ListItem, ListItemText, Stack, Typography } from '@mui/material'
import SummarizeOutlinedIcon from '@mui/icons-material/SummarizeOutlined'
import TaskAltOutlinedIcon from '@mui/icons-material/TaskAltOutlined'
import WarningAmberOutlinedIcon from '@mui/icons-material/WarningAmberOutlined'
import ListAltOutlinedIcon from '@mui/icons-material/ListAltOutlined'
import SkipNextOutlinedIcon from '@mui/icons-material/SkipNextOutlined'
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord'
import { formatRelativeTime } from '../../../utils/formatter'
import DeleteIcon from '@mui/icons-material/Delete'
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf'
import { downloadAsPdf } from '../../../utils/downloadAsPdf'

const AnalysisResult = ({ analysis, index, onDelete }) => {
    return (
        <Card variant="outlined">
            <CardContent>
                <Stack spacing={3}>

                    {/* Analysis Header */}
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        sx={{
                            justifyContent: "space-between",
                            alignItems: {
                                xs: "flex-start",
                                sm: "center",
                            },
                            gap: 1,
                        }}
                    >
                        <Stack spacing={0.5}>
                            <Typography variant="h6" sx={{ fontWeight: 800 }}>
                                Analysis #{index}
                            </Typography>

                            <Typography variant="body2" color="textSecondary">
                                {analysis.provider}
                                <FiberManualRecordIcon sx={{ fontSize: 8, marginX: 1 }} />
                                {analysis.model}
                            </Typography>
                            <Typography color='textSecondary' sx={{ fontSize: 12 }}>{formatRelativeTime(analysis.createdAt)}</Typography>
                        </Stack>

                        <Typography variant="body2" color="success" sx={{ fontWeight: 600 }}>
                            {analysis.status}
                        </Typography>
                    </Stack>

                    <Divider />

                    {/* Summary */}
                    <Stack>
                        <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                            <SummarizeOutlinedIcon color='action' />
                            <Typography color='textPrimary' sx={{ fontWeight: 800 }}>
                                Summary
                            </Typography>
                        </Stack>
                        <Typography sx={{ marginTop: 0.5, paddingLeft: 4 }}>
                            {analysis.summary}
                        </Typography>
                    </Stack>

                    {/* Action Items */}
                    {analysis.actionItems?.length > 0 && (
                        <Stack>
                            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                                <TaskAltOutlinedIcon color="error" />
                                <Typography color='error' sx={{ fontWeight: 800 }}>
                                    Action Items
                                </Typography>
                            </Stack>

                            <List dense sx={{ margin: 0, padding: 0 }}>
                                {analysis.actionItems.map((item, itemIndex) => (

                                    <ListItem key={itemIndex} sx={{ alignItems: 'flex-start', paddingLeft: 4 }}>
                                        <ListItemText
                                            primary={`${item.assignee}: ${item.task}`}
                                            secondary={`Deadline: ${item.deadline}`}
                                        />
                                    </ListItem>
                                ))}
                            </List>
                        </Stack>
                    )}

                    {/* Decisions */}
                    {analysis.decisions?.length > 0 && (
                        <Stack >
                            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                                <ListAltOutlinedIcon color='action' />
                                <Typography color='textPrimary' sx={{ fontWeight: 800 }}>
                                    Decisions
                                </Typography>
                            </Stack>

                            <List dense sx={{ margin: 0, padding: 0 }}>
                                {analysis.decisions.map((decision, decisionIndex) => (

                                    <ListItem key={decisionIndex} sx={{ alignItems: 'flex-start', paddingLeft: 4 }}>
                                        <ListItemText primary={decision} />
                                    </ListItem>
                                ))}
                            </List>
                        </Stack>
                    )}

                    {/* Risks */}
                    {analysis.risks?.length > 0 && (
                        <Stack>
                            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                                <WarningAmberOutlinedIcon color="warning" />
                                <Typography color='warning' sx={{ fontWeight: 800 }}>
                                    Risks
                                </Typography>
                            </Stack>

                            <List dense sx={{ margin: 0, padding: 0 }}>
                                {analysis.risks.map((risk, riskIndex) => (
                                    <ListItem key={riskIndex} sx={{ alignItems: 'flex-start', paddingLeft: 4 }}>
                                        <ListItemText primary={risk} />
                                    </ListItem>
                                ))}
                            </List>
                        </Stack>
                    )}

                    {/* Next Steps */}
                    {analysis.nextSteps?.length > 0 && (
                        <Stack >
                            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                                <SkipNextOutlinedIcon color='action' />
                                <Typography color='textPrimary' sx={{ fontWeight: 800 }}>
                                    Next Steps
                                </Typography>
                            </Stack>

                            <List dense sx={{ margin: 0, padding: 0 }}>
                                {analysis.nextSteps.map((step, stepIndex) => (
                                    <ListItem key={stepIndex} sx={{ alignItems: 'flex-start', paddingLeft: 4 }}>
                                        <ListItemText primary={`${stepIndex + 1}. ${step}`} />
                                    </ListItem>
                                ))}
                            </List>
                        </Stack>
                    )}

                    <Divider />

                    {/* Metadata */}
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                    >
                        <Typography variant="caption" color="text.secondary">
                            Analysis version: {analysis.analysisVersion}
                        </Typography>

                        <Typography variant="caption" color="text.secondary">
                            Prompt version: {analysis.promptVersion}
                        </Typography>

                        <Typography variant="caption" color="text.secondary">
                            Processing time: {analysis.processingTimeMs} ms
                        </Typography>
                    </Stack>

                    <Divider />

                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row"
                        }}
                        spacing={2}
                        sx={{
                            justifyContent: "flex-end"
                        }}
                    >
                        <Button
                            variant="outlined"
                            startIcon={<PictureAsPdfIcon />}
                            onClick={() => downloadAsPdf(analysis)}
                        >
                            Download PDF
                        </Button>

                        <Button
                            variant="outlined"
                            color="error"
                            startIcon={<DeleteIcon />}
                            onClick={() => onDelete(analysis)}
                        >
                            Delete
                        </Button>
                    </Stack>
                </Stack>
            </CardContent>
        </Card>
    )
}

export default AnalysisResult

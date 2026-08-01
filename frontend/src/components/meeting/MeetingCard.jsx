import { Button, Card, CardActions, CardContent, Chip, Stack, Typography } from '@mui/material'
import CalendarTodayIcon from "@mui/icons-material/CalendarToday"
import DescriptionIcon from "@mui/icons-material/Description"
import VisibilityIcon from "@mui/icons-material/Visibility"
import PsychologyIcon from "@mui/icons-material/Psychology"
import DeleteIcon from "@mui/icons-material/Delete"
import { formatDate, formatRelativeTime } from '../../utils/formatter'
import { Link } from 'react-router-dom'

const MeetingCard = ({ meeting, onDelete }) => {
    return (
        <Card elevation={3} >
            <CardContent>
                <Stack
                    direction="row"
                    sx={{
                        justifyContent: "space-between",
                        alignItems: "center",
                        mb: 3
                    }}
                >
                    <Typography variant="h6">
                        {meeting.title}
                    </Typography>

                    <Chip
                        label={meeting.status}
                        color="primary"
                        size="small"
                    />
                </Stack>

                <Stack
                    direction="row"
                    sx={{
                        justifyContent: "space-between",
                        alignItems: "start"
                    }}
                >
                    <Stack
                        direction="row"
                        spacing={2}
                    >
                        <CalendarTodayIcon fontSize="small" />

                        <Stack spacing={0.5}>
                            <Typography variant="body2">
                                {formatDate(meeting.createdAt)}
                            </Typography>

                            <Typography color="textSecondary" sx={{ fontSize: '12px' }}>
                                {formatRelativeTime(meeting.createdAt)}
                            </Typography>
                        </Stack>
                    </Stack>

                    <Stack direction="row" spacing={1}>
                        <DescriptionIcon fontSize="small" />
                        <Typography variant="body2">
                            {meeting.attachmentCount} Files
                        </Typography>
                    </Stack>
                </Stack>
            </CardContent>

            <CardActions
                sx={{
                    justifyContent: "start",
                    alignItems: "center"
                }}
            >
                <Button
                    startIcon={<VisibilityIcon />}
                    component={Link}
                    to={`/meetings/${meeting.uuid}`}
                >
                    View
                </Button>

                <Button
                    startIcon={<PsychologyIcon />}
                    color="success"
                >
                    Analyze
                </Button>

                <Button
                    color="error"
                    startIcon={<DeleteIcon />}
                    onClick={() => onDelete(meeting)}
                >
                    Delete
                </Button>
            </CardActions>
        </Card>
    )
}

export default MeetingCard
import { Button, Card, CardContent, CircularProgress, Divider, Stack, Typography } from '@mui/material'
import { Link, useParams } from 'react-router-dom'
import { formatDateTime } from '../utils/formatter'
import { useGetMeeting } from '../hooks/useMeeting'
import ErrorResponse from '../components/common/ErrorResponse'
import SuccessResponse from '../components/common/SuccessResponse'
import EditIcon from '@mui/icons-material/Edit'
import UploadFileIcon from '@mui/icons-material/UploadFile'
import PsychologyIcon from '@mui/icons-material/Psychology'

const MeetingDetailsPage = () => {

    const { uuid } = useParams();

    const {
        data: meeting,
        isLoading,
        error
    } = useGetMeeting(uuid);

    if (isLoading) {
        return (
            <SuccessResponse icon={<CircularProgress />} />
        );
    }

    if (error) {
        return (
             <ErrorResponse error_msg={!error.response ? "Cannot connect to server." : error.response.data.message} />
        );
    }

    return (

        <Card>
            <CardContent>
                <Typography
                    variant="h4"
                    gutterBottom
                >
                    {meeting.title}
                </Typography>

                <Typography
                    color="textSecondary"
                >
                    {meeting.description || "No description"}
                </Typography>

                <Divider sx={{ my: 3 }} />

                <Stack spacing={2}>
                    <Typography>
                        <strong>Status:</strong> {meeting.status}
                    </Typography>

                    <Typography>
                        <strong>Attachments:</strong> {meeting.attachmentCount}
                    </Typography>

                    <Typography>
                        <strong>Analyses:</strong> {meeting.analysisCount}
                    </Typography>

                    <Typography>
                        <strong>Created:</strong>{" "} {formatDateTime(meeting.createdAt)}
                    </Typography>

                    <Typography>
                        <strong>Updated:</strong>{" "} {formatDateTime(meeting.updatedAt)}
                    </Typography>
                </Stack>

                <Divider sx={{ my: 3 }} />

                <Stack
                    direction="row"
                    spacing={2}
                >
                    <Button
                        component={Link}
                        to={`/meetings/${uuid}/edit`}
                        variant="contained"
                        startIcon={<EditIcon />}
                    >
                        Edit
                    </Button>

                    <Button
                        variant="outlined"
                        startIcon={<UploadFileIcon />}
                    >
                        Upload
                    </Button>

                    <Button
                        variant="contained"
                        color="secondary"
                        startIcon={<PsychologyIcon />}
                    >
                        Analyze
                    </Button>
                </Stack>
            </CardContent>
        </Card>
    )
}

export default MeetingDetailsPage
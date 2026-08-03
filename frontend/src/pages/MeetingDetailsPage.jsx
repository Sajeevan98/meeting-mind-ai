import { Box, Button, Card, CardContent, CircularProgress, Divider, Stack, Tab, Tabs, Typography } from '@mui/material'
import { Link, useParams, useSearchParams } from 'react-router-dom'
import { useGetMeeting } from '../hooks/useMeeting'
import ErrorResponse from '../components/common/ErrorResponse'
import SuccessResponse from '../components/common/SuccessResponse'
import EditIcon from '@mui/icons-material/Edit'
import Source from '@mui/icons-material/Source'
import PsychologyIcon from '@mui/icons-material/Psychology'
import MeetingOverview from '../components/meeting/taps/MeetingOverview'
import MeetingAttachments from '../components/meeting/taps/MeetingAttachments'
import MeetingAnalysis from '../components/meeting/taps/MeetingAnalysis'
import MeetingDetailsTabs from '../components/meeting/MeetingDetailsTabs'


const MeetingDetailsPage = () => {

    const { uuid } = useParams();

    const [searchParams, setSearchParams] = useSearchParams();

    const currentTab = searchParams.get("tab") || "overview";

    const {
        data: meeting,
        isLoading,
        error
    } = useGetMeeting(uuid);

    if (isLoading) {
        return <SuccessResponse icon={<CircularProgress />} />
    }

    if (error) {
        return <ErrorResponse error_msg={!error.response ? "Cannot connect to server." : error.response.data.message} />
    }

    return (

        <Card>
            <CardContent>

                {/* Meeting Header  */}
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

                {/* Actions */}
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
                        variant="contained"
                        color='warning'
                        startIcon={<Source />}
                        onClick={() => setSearchParams({ tab: "attachments" })}
                    >
                        Attachments
                    </Button>

                    <Button
                        variant="contained"
                        color="secondary"
                        startIcon={<PsychologyIcon />}
                        onClick={() => setSearchParams({ tab: "analysis" })}
                    >
                        Analyze
                    </Button>
                </Stack>

                <Divider sx={{ my: 3 }} />

                {/* Navigation Tabs */}
                <MeetingDetailsTabs />

                {/* Tab Content */}
                {currentTab === "overview" && (<MeetingOverview meeting={meeting} />)}

                {currentTab === "attachments" && (<MeetingAttachments meeting={meeting} />)}

                {currentTab === "analysis" && (<MeetingAnalysis meeting={meeting} />)}

            </CardContent>
        </Card>
    )
}

export default MeetingDetailsPage
import { Container, Box, CircularProgress, Typography, Alert, Paper, Toolbar, Grid, Stack, Button } from '@mui/material'
import MeetingCard from '../components/meeting/MeetingCard'
import AddIcon from '@mui/icons-material/Add'
import { Link } from 'react-router-dom';
import ErrorResponse from '../components/common/ErrorResponse'
import SuccessResponse from '../components/common/SuccessResponse'
import { useSnackbar } from 'notistack'
import { useState } from 'react'
import DeleteDialog from '../components/common/DeleteDialog'
import { useDeleteMeeting, useGetMeetings } from '../hooks/useMeeting'

const MeetingListPage = () => {

    const {
        data,
        isLoading,
        error
    } = useGetMeetings();

    const meetings = data?.content ?? [];

    const [meetingToDelete, setMeetingToDelete] = useState(null);
    const deleteMeetingMutation = useDeleteMeeting();
    const { enqueueSnackbar } = useSnackbar();
    const [errorMsg, setErrorMsg] = useState("");

    if (isLoading) {
        return <SuccessResponse icon={<CircularProgress />} />
    }

    if (error) {
        return <ErrorResponse error_msg={"Failed to load meetings."} />
    }


    const handleDeleteClick = (meeting) => {

        setMeetingToDelete(meeting);
    };

    const handleDeleteCancel = () => {

        setErrorMsg("");
        
        if (deleteMeetingMutation.isPending) {
            return;
        }

        setMeetingToDelete(null);
    };

    const handleDeleteConfirm = () => {

        if (!meetingToDelete) {
            return;
        }

        deleteMeetingMutation.mutate(meetingToDelete.uuid, {

            onSuccess: () => {

                setErrorMsg("");

                enqueueSnackbar(
                    "Meeting deleted successfully.",
                    {
                        variant: "success"
                    }
                );

                setMeetingToDelete(null);
            },

            onError: (error) => {

                setErrorMsg(error?.response?.data?.message ?? "Failed to delete Meeting");
            }
        });
    };

    return (
        <Container maxWidth="xl">
            <Stack
                direction="row"
                sx={{
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 3
                }}
            >
                <Typography
                    variant="h4"
                    fontWeight={700}
                    mb={3}
                >
                    Meetings
                </Typography>

                <Button
                    component={Link}
                    to="/meetings/new"
                    variant="contained"
                    startIcon={<AddIcon />}
                    sx={{
                        fontSize: {
                            md: '18px'
                        },
                        fontWeight: {
                            md: '600'
                        }
                    }}
                >
                    New Meeting
                </Button>
            </Stack>

            {
                meetings.length === 0 ? (

                    <Alert
                        severity="info"
                        sx={{
                            p: 2,
                            mt: 2

                        }}
                    >
                        No meetings found.
                    </Alert>

                ) : (

                    <Grid
                        container
                        spacing={3}
                        sx={{
                            mt: 2

                        }}
                    >
                        {
                            meetings.map((meeting) => (
                                <Grid
                                    size={{ xs: 12, md: 6, lg: 4 }}
                                    key={meeting.uuid}
                                >
                                    <MeetingCard
                                        meeting={meeting}
                                        onDelete={handleDeleteClick}
                                    />
                                </Grid>
                            ))
                        }
                    </Grid>
                )
            }

            <DeleteDialog
                open={!!meetingToDelete}
                property={meetingToDelete}
                loading={deleteMeetingMutation.isPending}
                onClose={handleDeleteCancel}
                onConfirm={handleDeleteConfirm}
                name="Meeting" // for reusable purpose
                errorMessage={errorMsg}
            />
        </Container>
    )
}

export default MeetingListPage
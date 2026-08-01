import { CircularProgress, Container, Paper, Typography } from '@mui/material'
import MeetingForm from '../components/meeting/MeetingForm'
import SuccessResponse from '../components/common/SuccessResponse'
import ErrorResponse from '../components/common/ErrorResponse'
import { useNavigate, useParams } from 'react-router-dom'
import { useUpdateMeeting, useGetMeeting } from '../hooks/useMeeting'
import { useSnackbar } from 'notistack'

const MeetingEditPage = () => {

    const { uuid } = useParams();

    const navigate = useNavigate();

    const { enqueueSnackbar } = useSnackbar();

    const {

        data: meeting,
        isLoading,
        error
    } = useGetMeeting(uuid);

    const updateMeetingMutation = useUpdateMeeting();

    const handleSubmit = (formData) => {

        updateMeetingMutation.mutate(
            {
                uuid,
                meeting: formData
            },
            {
                onSuccess: () => {

                    enqueueSnackbar(
                        "Meeting updated successfully.",
                        {
                            variant: "success"
                        }
                    );
                    navigate(`/meetings/${uuid}`);
                },

                onError: (error) => {

                    enqueueSnackbar(
                        error?.response?.data?.message ?? "Failed to update meeting.",
                        {
                            variant: "error"
                        }
                    );
                }
            }
        );
    };


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

        <Container maxWidth="md">
            <Paper sx={{ p: 3 }}>

                <Typography
                    variant="h5"
                    mb={3}
                >
                    Edit Meeting
                </Typography>

                <MeetingForm
                    initialValues={meeting}
                    onSubmit={handleSubmit}
                    loading={updateMeetingMutation.isPending}
                />
            </Paper>
        </Container>
    )
}

export default MeetingEditPage
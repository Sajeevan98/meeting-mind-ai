import { useNavigate } from 'react-router-dom'
import { useCreateMeeting } from '../hooks/useMeeting'
import { Container, Paper, Typography } from '@mui/material'
import MeetingForm from '../components/meeting/MeetingForm'
import { useSnackbar } from 'notistack'

const CreateMeetingPage = () => {

    const navigate = useNavigate();

    const createMeeting = useCreateMeeting();

    const { enqueueSnackbar } = useSnackbar();

    const handleSubmit = (data) => {

        // console.log(`meeting ==> ${JSON.stringify(data)}`);

        createMeeting.mutate(data, {

            onSuccess: () => {

                enqueueSnackbar(
                    "Meeting created successfully.",
                    {
                        variant: "success"
                    }
                );

                navigate("/meetings");
            },

            onError: (error) => {

                enqueueSnackbar(
                    error?.response?.data?.message ?? "Failed to create meeting.",
                    {
                        variant: "error"
                    }
                );
            }
        });
    };

    return (
        <Container maxWidth="md">
            <Paper sx={{ p: 4 }}>

                <Typography
                    variant="h5"
                    gutterBottom
                >
                    Create Meeting
                </Typography>

                <MeetingForm
                    onSubmit={handleSubmit}
                    loading={createMeeting.isPending}
                />
            </Paper>
        </Container>
    )
}

export default CreateMeetingPage
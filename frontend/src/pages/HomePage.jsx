import { Typography, Grid, Card, CardContent } from '@mui/material'
import { useGetMeetings } from '../hooks/useMeeting'

export default function HomePage() {

  const {
    data,
    isLoading,
    error
  } = useGetMeetings();

  const meetings = data?.content ?? [];

  const meetingsCounts = meetings.length > 0 ? meetings.length : 0;

  const totalAttachmentCount = meetings.reduce(
    (total, meeting) => total + (meeting.attachmentCount ?? 0), 0
  );

  const totalAnalysisCounts = meetings.reduce(
    (total, meeting) => total + (meeting.analysisCount ?? 0), 0
  );

  return (

    <>
      <Typography
        variant="h4"
        gutterBottom
        color="warning"
      >
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        {[

          "Meetings",
          "Documents",
          "Analyses"

        ].map(title => (

          <Grid
            key={title}
            size={{ xs: 12, md: 4 }}
          >
            <Card>
              <CardContent>

                <Typography variant="h6" color="textPrimary">
                  {title}
                </Typography>

                <Typography variant="h4" color="warning">
                  {title === "Meetings" && meetingsCounts}

                  {title === "Documents" && totalAttachmentCount}

                  {title === "Analyses" && totalAnalysisCounts}
                </Typography>

              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </>
  );
}
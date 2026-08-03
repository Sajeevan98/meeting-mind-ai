import { Stack, Typography } from '@mui/material'
import { formatDateTime } from '../../../utils/formatter'

const MeetingOverview = ({ meeting }) => {

  return (
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
  )
}

export default MeetingOverview
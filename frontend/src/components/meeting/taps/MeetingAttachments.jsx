import { useGetAttachments, useDeleteAttachment } from '../../../hooks/useAttachment'
import SuccessResponse from '../../common/SuccessResponse'
import ErrorResponse from '../../common/ErrorResponse'
import { formatDateTime, formatRelativeTime } from '../../../utils/formatter'

import { Alert, Box, Button, Card, CardContent, CircularProgress, Divider, IconButton, Stack, Typography } from '@mui/material'
import UploadFileIcon from '@mui/icons-material/UploadFile'
import DeleteIcon from '@mui/icons-material/Delete'
import DescriptionIcon from '@mui/icons-material/Description'
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf'
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile'
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord'
import { useState } from 'react'
import MeetingUploadDialog from '../MeetingUploadDialog'
import DeleteDialog from '../../common/DeleteDialog'
import { useSnackbar } from 'notistack'

const MeetingAttachments = ({ meeting }) => {

    const [uploadOpen, setUploadOpen] = useState(false);

    const [attachmentToDelete, setAttachmentToDelete] = useState(null);

    const [errorMsg, setErrorMsg] = useState("");

     const { enqueueSnackbar } = useSnackbar();

    const {
        data: attachments,
        isLoading,
        error
    } = useGetAttachments(meeting.uuid);

    const deleteMutation = useDeleteAttachment(meeting.uuid);

    const handleDeleteClick = (attachment) => {

        setAttachmentToDelete(attachment);
    };

    const handleDeleteCancel = () => {

        if (deleteMutation.isPending)
            return;
        setAttachmentToDelete(null);
        setErrorMsg("");
    };

    const handleDeleteConfirm = () => {

        if (!attachmentToDelete)
            return;

        deleteMutation.mutate(attachmentToDelete.uuid, {

            onSuccess: () => {

                enqueueSnackbar(
                    "Attachment deleted successfully.",
                    {
                        variant: "success"
                    }
                );

                setAttachmentToDelete(null);
                setErrorMsg("");
            },

            onError: (error) => {

                setErrorMsg(error?.response?.data?.message ?? "Failed to delete attachment");
            }
        });
    };

    if (isLoading) {
        return <SuccessResponse icon={<CircularProgress />} />
    }

    if (error) {
        return <ErrorResponse error_msg={"Failed to load Attachments."} />
    }

    const attachmentList = attachments ?? [];

    // file icon picker
    const getFileIcon = (attachment) => {

        if (attachment.fileExtension?.toLowerCase() === "pdf") {

            return <PictureAsPdfIcon color="error" />;
        }
        if (attachment.fileExtension?.toLowerCase() === "doc" || attachment.fileExtension?.toLowerCase() === "docx") {

            return <DescriptionIcon color="primary" />;
        }

        return <InsertDriveFileIcon />;
    };

    // file size formatter
    const formatFileSize = (bytes) => {

        if (!bytes) {

            return "0 Bytes";
        }
        if (bytes < 1024) {

            return `${bytes} Bytes`;
        }
        if (bytes < 1024 * 1024) {

            return `${(bytes / 1024).toFixed(1)} KB`;
        }

        return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
    };


    return (

        <Stack spacing={3}>

            {/* Header */}
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'baseline', paddingX: 2 }} >
                <Box>
                    <Typography variant="h6" color='warning' sx={{ fontWeight: '600' }} >
                        Meeting Attachments
                    </Typography>

                    <Typography variant="body2" color='textSecondary' >
                        {attachmentList.length} file{attachmentList.length !== 1 ? "s" : ""}
                    </Typography>
                </Box>

                {/* upload button */}
                <Button
                    variant="outlined"
                    color='warning'
                    startIcon={<UploadFileIcon />}
                    onClick={() => setUploadOpen(true)}
                >
                    Upload
                </Button>
            </Stack>

            <Divider />

            {/* Empty State */}
            {attachmentList.length === 0 &&
                (
                    <Alert severity="info" icon={<UploadFileIcon />} >
                        No attachments have been uploaded for this meeting yet.
                    </Alert>
                )
            }

            {/* Attachment List */}
            {attachmentList.length > 0 &&
                (
                    <Stack spacing={2}>
                        {attachmentList.map((attachment) => (
                            <Card key={attachment.uuid} variant="outlined" >
                                <CardContent>
                                    <Stack
                                        direction="row"
                                        spacing={2}
                                        sx={{
                                            justifyContent: 'space-between',
                                            alignItems: 'center'
                                        }}
                                    >
                                        {/* File information */}
                                        <Stack
                                            direction="row"
                                            spacing={2}
                                            sx={{
                                                minWidth: 0,
                                                alignItems: 'center'
                                            }}
                                        >
                                            {getFileIcon(attachment)}
                                            <Box>
                                                <Typography sx={{ fontWeight: '600', mb: 0.5 }} noWrap>
                                                    {attachment.originalFileName}
                                                </Typography>

                                                <Typography variant="body2" color='textSecondary'>
                                                    <FiberManualRecordIcon sx={{ fontSize: 5, marginRight: 0.5, marginLeft: 0 }} />
                                                    {formatFileSize(attachment.fileSize)}

                                                    <FiberManualRecordIcon sx={{ fontSize: 5, marginRight: 0.5, marginLeft: 1.5 }} />
                                                    {formatDateTime(attachment.createdAt)}

                                                    <FiberManualRecordIcon sx={{ fontSize: 3, marginRight: 0.5, marginLeft: 1.5 }} />
                                                    <i style={{ fontSize: 12 }}>{formatRelativeTime(attachment.createdAt)}</i>
                                                </Typography>
                                            </Box>
                                        </Stack>

                                        {/* Actions */}
                                        <Stack direction="row" spacing={1}>
                                            <IconButton
                                                color="error"
                                                aria-label="delete attachment"
                                                onClick={() => handleDeleteClick(attachment)}
                                                disabled={deleteMutation.isPending}
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </Stack>
                                    </Stack>
                                </CardContent>
                            </Card>))}
                    </Stack>
                )
            }

            <MeetingUploadDialog
                open={uploadOpen}
                meetingUuid={meeting.uuid}
                onClose={() => setUploadOpen(false)}
            />

            <DeleteDialog
                open={!!attachmentToDelete}
                property={attachmentToDelete} // attachment
                loading={deleteMutation.isPending}
                onClose={handleDeleteCancel}
                onConfirm={handleDeleteConfirm}
                name="Attachment" // for Dialog-Component reusable purpose
                errorMessage={errorMsg}
            />
        </Stack>
    );
};

export default MeetingAttachments
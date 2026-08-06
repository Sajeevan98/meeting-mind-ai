import { useState } from 'react'
import UploadFileIcon from '@mui/icons-material/UploadFile'
import { useCreateAttachment } from '../../hooks/useAttachment'
import { Alert, Button, Dialog, DialogActions, DialogContent, DialogTitle, Stack, Typography } from '@mui/material'
import { useSnackbar } from 'notistack';


const MeetingUploadDialog = ({ open, meetingUuid, onClose }) => {

    const [file, setFile] = useState(null);

    const [errorMessage, setErrorMessage] = useState("");

    const uploadMutation = useCreateAttachment(meetingUuid);

    const { enqueueSnackbar } = useSnackbar();


    const handleFileChange = (event) => {

        const selectedFile = event.target.files?.[0];

        if (!selectedFile) {
            return;
        }

        setFile(selectedFile);
        setErrorMessage("");
    };

    const handleUpload = () => {

        if (!file) {

            setErrorMessage("Please select a file.");
            return;
        }

        const formData = new FormData();

        formData.append("uuid", meetingUuid);
        formData.append("file", file);

        // for (const [key, value] of formData.entries()) {
        //     console.log(key, value);
        // }

        uploadMutation.mutate(formData, {

            onSuccess: () => {

                enqueueSnackbar(
                    "File uploaded successfully.",
                    {
                        variant: "success"
                    }
                );

                setFile(null);
                setErrorMessage("");
                onClose();
            },

            onError: (error) => {

                setErrorMessage(
                    !error.response ? "Cannot connect to server."
                        : error.response.data?.message || "Failed to upload attachment."
                );
            }
        });
    };

    const handleClose = () => {

        if (uploadMutation.isPending) {
            return;
        }

        setFile(null);
        setErrorMessage("");

        onClose();
    };

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            fullWidth
            maxWidth="sm"
        >
            <DialogTitle>
                Upload Meeting Attachment
            </DialogTitle>

            <DialogContent>
                <Stack spacing={3} sx={{ mt: 1 }}>
                    {errorMessage &&
                        (
                            <Alert severity="error">
                                {errorMessage}
                            </Alert>
                        )
                    }
                    <Button
                        component="label"
                        variant="outlined"
                        startIcon={<UploadFileIcon />}
                        disabled={uploadMutation.isPending}
                    >
                        Select File
                        <input
                            type="file"
                            hidden
                            aria-label="select file"
                            onChange={handleFileChange}
                        />
                    </Button>

                    {file && (
                        <Typography
                            variant="body2"
                            color="text.secondary"
                        >
                            Selected file:{" "}
                            <strong>
                                {file.name}
                            </strong>
                        </Typography>
                    )}
                </Stack>
            </DialogContent>

            <DialogActions>
                <Button
                    onClick={handleClose}
                    disabled={uploadMutation.isPending}
                >
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    onClick={handleUpload}
                    disabled={
                        !file ||
                        uploadMutation.isPending
                    }
                >
                    {uploadMutation.isPending
                        ? "Uploading..."
                        : "Upload"
                    }
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default MeetingUploadDialog

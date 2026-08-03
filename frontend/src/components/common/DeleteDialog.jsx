import { Alert, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material'

const DeleteDialog = ({ open, property, loading, onClose, onConfirm, name, errorMessage }) => {

    // console.log("property ==> ", property);
    // console.log("name ==> ", name);

    return (
        <Dialog
            open={open}
            onClose={loading ? undefined : onClose}
            maxWidth="sm"
            fullWidth
        >
            <DialogTitle>
                Delete {name}
            </DialogTitle>

            <DialogContent>
                {errorMessage &&
                    (
                        <Alert severity="error">
                            {errorMessage}
                        </Alert>
                    )
                }
                <DialogContentText>
                    Are you sure you want to delete{" "}
                    {name.toUpperCase() === "MEETING" && <strong>  {property?.title}</strong>}
                    {name.toUpperCase() === "ATTACHMENT" && <strong>  {property?.originalFileName}</strong>}
                     {name.toUpperCase() === "ANALYSIS" && <strong>  Analysis Version -{property?.analysisVersion}</strong>}
                    ?
                    <br />
                    This action cannot be undone.
                    {
                        name.toUpperCase() === "MEETING" &&
                        <><br /><br /> <i>Remember, when you delete the meeting, the attachments are also deleted.</i></>
                    }
                </DialogContentText>
            </DialogContent>

            <DialogActions>
                <Button
                    onClick={onClose}
                    disabled={loading}
                >
                    Cancel
                </Button>

                <Button
                    onClick={onConfirm}
                    color="error"
                    variant="contained"
                    disabled={loading}
                >
                    {loading
                        ? "Deleting..."
                        : "Delete"
                    }
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default DeleteDialog
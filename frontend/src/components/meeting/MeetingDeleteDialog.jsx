import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material'

const MeetingDeleteDialog = ({ open, meeting, loading, onClose, onConfirm }) => {

    return (
        <Dialog
            open={open}
            onClose={loading ? undefined : onClose}
            maxWidth="sm"
            fullWidth
        >
            <DialogTitle>
                Delete Meeting
            </DialogTitle>

            <DialogContent>
                <DialogContentText>
                    Are you sure you want to delete{" "}
                    <strong>
                        {meeting?.title}
                    </strong>
                    ?
                    <br />
                    This action cannot be undone.
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

export default MeetingDeleteDialog
import { Alert, Container } from '@mui/material'

const ErrorResponse = ({ error_msg }) => {
    return (
        <Container maxWidth="xl">
            <Alert severity="error">
                {error_msg}
            </Alert>
        </Container>
    )
}

export default ErrorResponse
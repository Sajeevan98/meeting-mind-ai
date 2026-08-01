import { Box, Container } from '@mui/material'

const SuccessResponse = ({ icon }) => {
    return (
        <Container maxWidth="xl">
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "70vh"
                }}
            >
                {icon}
            </Box>
        </Container>
    )
}

export default SuccessResponse
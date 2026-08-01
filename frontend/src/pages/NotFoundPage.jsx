import { Alert, Container } from '@mui/material'
import React from 'react'

const NotFoundPage = () => {
  return (
    <Container maxWidth="xl">
      <Alert
        severity="error"
        sx={{

          fontSize: { md: '22px' },

          "& .MuiAlert-icon": {

            fontSize: { md: '38px' }
          }
        }}
      >
        404 - Page Not Found.
      </Alert>
    </Container>
  )
}

export default NotFoundPage
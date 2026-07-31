import { createTheme } from '@mui/material'
import React from 'react'

const theme = createTheme({

    palette: {

        primary: {

            main: "#1976d2"
        },
        secondary: {

            main: "#2e7d32"
        },
        background: {

            default: "#f5f7fa"
        }
    },
    typography: {

        fontFamily: "Roboto, sans-serif"
    }
})

export default theme
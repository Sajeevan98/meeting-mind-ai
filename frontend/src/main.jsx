import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClientProvider } from '@tanstack/react-query'

import App from './App'
import {queryClient} from "./api/queryClient"
import { CssBaseline, ThemeProvider } from '@mui/material'
import theme from './theme/theme'

createRoot(document.getElementById('root')).render(
  <StrictMode>

    <QueryClientProvider client={queryClient}>

      <ThemeProvider theme={theme}>

        <CssBaseline />

         <App/>

      </ThemeProvider>

    </QueryClientProvider>

  </StrictMode>,
)

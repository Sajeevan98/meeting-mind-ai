import { SnackbarProvider } from 'notistack'
import AppRouter from './routes/AppRouter'

const App = () => {

  return (
    <SnackbarProvider
      maxSnack={3}            // maximum toast at a time
      autoHideDuration={5000} // 5 Sec
      anchorOrigin={{
        vertical: "top",
        horizontal: "right"
      }}
    >
      <AppRouter />
    </SnackbarProvider>
  )
}

export default App


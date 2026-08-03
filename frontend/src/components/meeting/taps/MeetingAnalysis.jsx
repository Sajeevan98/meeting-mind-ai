import { Alert, CircularProgress, Stack, Typography } from '@mui/material'
import { useState } from 'react'
import { useCreateAnalysis, useGetAnalyses, useDeleteAnalysis } from '../../../hooks/useAnalysis'
import PsychologyIcon from '@mui/icons-material/Psychology'
import AnalysisResult from '../analysis/AnalysisResult'
import AnalysisCard from '../analysis/AnalysisCard'
import { useSnackbar } from 'notistack'
import DeleteDialog from '../../common/DeleteDialog'


const MeetingAnalysis = ({ meeting }) => {

  const [provider, setProvider] = useState("GEMINI");
  const [model, setModel] = useState("gemini-3.1-flash-lite");
  const [analysisToDelete, setAnalysisToDelete] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");

  const { enqueueSnackbar } = useSnackbar();

  const createAnalysisMutation = useCreateAnalysis(meeting.uuid);
  const deleteAnalysisMutation = useDeleteAnalysis(meeting.uuid);
  const {
    data: analyses = [],
    isLoading: analysisLoading,
    error: analysesError
  } = useGetAnalyses(meeting.uuid);

  // get the recent analysis
  const latestAnalysis = analyses.length > 0 ? analyses[0] : null;

  const handleProviderChange = (event) => {

    const newProvider = event.target.value;

    setProvider(newProvider);

    setModel(newProvider === "GEMINI" ? "gemini-3.1-flash-lite" : "gpt-4.1");
  };


  const handleAnalyze = () => {

    createAnalysisMutation.mutate({

      aiProvider: provider,
      model: model
    }, {
      onSuccess: () => {
        enqueueSnackbar(
          "Analysis created successfully.",
          {
            variant: "success"
          }
        );
      }
    }, {
      onError: (error) => {

        enqueueSnackbar(
          error?.response?.data?.message ?? "Failed to analysis.",
          {
            variant: "error"
          }
        );
      }
    }
    );
  };

  const handleDeleteClick = (analysis) => {

    setAnalysisToDelete(analysis);
  };


  const handleDeleteCancel = () => {

    if (deleteAnalysisMutation.isPending) {
      return;
    }
    setAnalysisToDelete(null);
    setErrorMsg("");
  };

  const handleDeleteConfirm = () => {

    if (!analysisToDelete) {
      return;
    }

    deleteAnalysisMutation.mutate(

      { analysisUuid: analysisToDelete.uuid },
      {
        onSuccess: () => {

          enqueueSnackbar(
            "Analysis deleted successfully.",
            {
              variant: "success"
            }
          );
          setAnalysisToDelete(null);
          setErrorMsg("");
        },

        onError: (error) => {
          setErrorMsg(error?.response?.data?.message ?? "Failed to analysis");
        }
      }
    );
  };

  return (

    <Stack spacing={3}>

      {/* Header */}
      <Stack spacing={0.5}>
        <Typography variant="h6" fontWeight={600}>
          AI Analysis
        </Typography>
        <Typography variant="body2" color="textSecondary">
          Generate structured insights from this meeting.
        </Typography>
      </Stack>

      {/* Analysis Configuration */}
      <AnalysisCard
        provider={provider}
        model={model}
        onProviderChange={handleProviderChange}
        onModelChange={(e) => setModel(e.target.value)}
        onAnalyze={handleAnalyze}
        mutation={createAnalysisMutation}
      />

      {/* Existing analyses */}
      {analysesError &&
        (
          <Alert severity="error">

            {analysesError?.response?.data?.message ??
              "Failed to load analysis history."}
          </Alert>
        )
      }
      {analysisLoading ?
        (
          <Stack sx={{ py: 4, alignItems: 'center', justifyContent: 'center' }}>
            <CircularProgress />
          </Stack>
        ) : analyses.length === 0 ? (
          <Alert severity="info">
            No analysis has been generated for this meeting yet.
          </Alert>
        ) : (
          <Typography>
            {analyses.length} {analyses.length !== 1 ? "analyses" : "analysis"} available.
          </Typography>
        )
      }
      {
        !analysisLoading && analyses.length > 0 && (
          <Stack spacing={3}>
            {
              analyses.map((analysis, index) => (

                <AnalysisResult
                  key={analysis.uuid}
                  analysis={analysis}
                  index={analyses.length - index}
                  onDelete={handleDeleteClick}
                />
              ))
            }
          </Stack>
        )
      }
      <DeleteDialog
        open={!!analysisToDelete}
        property={analysisToDelete} // analysis
        loading={deleteAnalysisMutation.isPending}
        onClose={handleDeleteCancel}
        onConfirm={handleDeleteConfirm}
        name="Analysis" // for Dialog-Component reusable purpose
        errorMessage={errorMsg}
      />
    </Stack>
  )
}

export default MeetingAnalysis
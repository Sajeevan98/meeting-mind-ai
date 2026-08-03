import { Alert, Button, Card, CardContent, CircularProgress, FormControl, InputLabel, MenuItem, Select, Stack, Typography } from '@mui/material'
import PsychologyIcon from '@mui/icons-material/Psychology'

const GEMINI_MODELS = [
    {
        value: "gemini-3.1-flash-lite",
        label: "Gemini-3.1-flash-lite",
    },
    {
        value: "gemini-3.1-flash-lite-preview",
        label: "Gemini-3.1-flash-lite-preview",
    },
    {
        value: "gemini-3.5-flash",
        label: "Gemini-3.5-flash",
    },
    {
        value: "gemini-3.5-flash-lite",
        label: "Gemini-3.5-flash-lite",
    },
    {
        value: "gemini-3.6-flash",
        label: "Gemini-3.6-flash",
    },
];

const OPENAI_MODELS = [
    {
        value: "gpt-4.1",
        label: "GPT-4.1",
    },
    {
        value: "gpt-5",
        label: "GPT-5",
    },
];

const AnalysisCard = ({ provider, model, onProviderChange, onModelChange, onAnalyze, mutation, }) => {

    const models = provider === "GEMINI" ? GEMINI_MODELS : OPENAI_MODELS;

    return (
        <Card variant="outlined">
            <CardContent>
                <Stack spacing={3}>

                    <Typography variant="subtitle1" fontWeight={600}>
                        Generate New Analysis
                    </Typography>

                    <FormControl>
                        <InputLabel>AI Provider</InputLabel>
                        <Select
                            value={provider}
                            label="AI Provider"
                            onChange={onProviderChange}
                        >
                            <MenuItem value="GEMINI">Gemini</MenuItem>
                            <MenuItem value="OPENAI">OpenAI</MenuItem>
                        </Select>
                    </FormControl>

                    <FormControl>
                        <InputLabel>Model</InputLabel>
                        <Select value={model} label="Model" onChange={onModelChange}>
                            {models.map((option) => (
                                <MenuItem key={option.value} value={option.value}>
                                    {option.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    {mutation.isError && (
                        <Alert severity="error">
                            {mutation.error?.response?.data?.message ??
                                "Failed to analyze meeting."}
                        </Alert>
                    )}

                    <Button
                        variant="contained"
                        color="secondary"
                        startIcon={
                            mutation.isPending ? (
                                <CircularProgress size={20} color="inherit" />
                            ) : (
                                <PsychologyIcon />
                            )
                        }
                        onClick={onAnalyze}
                        disabled={mutation.isPending}
                    >
                        {mutation.isPending ? "Analyzing..." : "Analyze Meeting"}
                    </Button>
                </Stack>
            </CardContent>
        </Card>
    );
};

export default AnalysisCard
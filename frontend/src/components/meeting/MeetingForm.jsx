import { Box, Button, Stack, TextField } from '@mui/material'
import { useEffect } from 'react';
import { useForm } from 'react-hook-form'

const MeetingForm = ({ initialValues = null, onSubmit, loading = false }) => {

    const {

        register,
        handleSubmit,
        reset,
        formState: { errors }

    } = useForm({

        // When the form is created
        defaultValues: {
            title: "",
            description: ""
        }
    });

    useEffect(() => {

        if (initialValues) {

            // Replace the current form values with new values
            reset({
                title: initialValues.title ?? "",
                description: initialValues.description ?? ""
            });
        }
    }, [initialValues, reset]);

    return (
        <Box
            component="form"
            onSubmit={handleSubmit(onSubmit)}
            sx={{
                mt: 3
            }}
        >
            <Stack spacing={3}>

                <TextField
                    label="Title"
                    fullWidth
                    {...register("title", {

                        required: "Title is required",

                        minLength: {
                            value: 3,
                            message: "Minimum 3 characters"
                        }
                    })}
                    error={!!errors.title}
                    helperText={errors.title?.message}
                />

                <TextField
                    label="Description"
                    fullWidth
                    multiline
                    rows={4}
                    {...register("description", {

                        required: "Dscription Field Can not be empty! Just add something about Meeting.",

                        maxLength: {

                            value: 500,
                            message: "Description cannot exceed 500 characters."
                        }
                    })}
                    error={!!errors.description}
                    helperText={errors.description?.message}
                />

                <Button
                    type="submit"
                    variant="contained"
                    disabled={loading}
                >
                    {loading
                        ? "Saving..."
                        : initialValues
                            ? "Update Meeting"
                            : "Create Meeting"}
                </Button>
            </Stack>
        </Box>
    )
}

export default MeetingForm
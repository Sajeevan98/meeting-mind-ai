import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { createAnalysis, deleteAnalysis, getAnalyses, getAnalysis } from "../services/analysisService"

// get analysis history for a meeting
export const useGetAnalyses = (meetingUuid) => {

    return useQuery({

        queryKey: ["analyses", meetingUuid],

        queryFn: () => getAnalyses(meetingUuid),

        enabled: !!meetingUuid
    });
}

// get single analysis
export const useGetAnalysis = (analysisUuid) => {

    return useQuery({

        queryKey: ["analysis", analysisUuid],

        queryFn: () => getAnalysis(analysisUuid),

        enabled: !!analysisUuid
    });
}

// create / start AI analysis
export const useCreateAnalysis = (meetingUuid) => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: (request) => createAnalysis(meetingUuid, request),

        onSuccess: () => {

            // refresh analyses list
            client.invalidateQueries({
                queryKey: ["analyses", meetingUuid]
            });

            // refresh this meeting (analysisCount updates)
            client.invalidateQueries({
                queryKey: ["meeting", meetingUuid]
            });

            // refresh meeting list
            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    })
}


// delete analysis
export const useDeleteAnalysis = (meetingUuid) => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: ({ analysisUuid }) => deleteAnalysis(analysisUuid),

        onSuccess: (_, variables) => {

            const { analysisUuid } = variables;

            client.invalidateQueries({
                queryKey: ["analyses", meetingUuid]
            });

            client.invalidateQueries({
                queryKey: ["meeting", meetingUuid]
            });

            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    });
}

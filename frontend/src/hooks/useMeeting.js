import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    getMeetings,
    getMeeting,
    createMeeting,
    updateMeeting,
    deleteMeeting
} from '../services/meetingService'


export const useGetMeetings = () => {

    return useQuery({

        queryKey: ["meetings"],

        queryFn: getMeetings
    });
}

export const useGetMeeting = (meetingUuid) => {

    return useQuery({

        queryKey: ["meeting", meetingUuid],

        queryFn: () => getMeeting(meetingUuid),

        enabled: !!meetingUuid // Only execute this query, when the UUID actually exists
    });
};

export const useCreateMeeting = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: createMeeting,

        onSuccess: () => {

            client.invalidateQueries({
                queryKey: ["meetings"]
            })
        }
    });
}

export const useUpdateMeeting = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: ({ meetingUuid, meeting }) => updateMeeting(meetingUuid, meeting),

        onSuccess: (_, variables) => {

            const { meetingUuid } = variables;
            
            // Refresh single meeting
            client.invalidateQueries({
                queryKey: ["meeting", meetingUuid]
            });

            // Refresh meeting list
            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    });
}

export const useDeleteMeeting = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: deleteMeeting,

        onSuccess: () => {

            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    });
};

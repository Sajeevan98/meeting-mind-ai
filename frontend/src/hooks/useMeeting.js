import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    getMeetings,
    getMeeting,
    createMeeting,
    updateMeeting,
    deleteMeeting
} from '../services/meetingService'


const useGetMeetings = () => {
    return useQuery({

        queryKey: ["meetings"],

        queryFn: getMeetings
    });
}

const useGetMeeting = (uuid) => {

    return useQuery({

        queryKey: ["meeting", uuid],

        queryFn: () => getMeeting(uuid),

        enabled: !!uuid // Only execute this query, when the UUID actually exists
    });
};

const useCreateMeeting = (meeting) => {

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

const useUpdateMeeting = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: ({ uuid, meeting }) =>
            updateMeeting(uuid, meeting),

        onSuccess: (data, updatedMeeting) => {

            // Update the individual meeting cache
            client.setQueryData(
                ["meeting", updatedMeeting.uuid], data
            );

            // Refetch the meeting list
            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    });
}

const useDeleteMeeting = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: deleteMeeting,

        onSuccess: (_, uuid) => {

            client.invalidateQueries({
                queryKey: ["meetings"]
            });

            client.removeQueries({
                queryKey: ["meeting", uuid]
            });
        }
    });
};

export {

    useGetMeetings,
    useGetMeeting,
    useCreateMeeting,
    useUpdateMeeting,
    useDeleteMeeting
}
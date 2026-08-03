import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    getAttachments,
    createAttachment,
    deleteAttachment
} from '../services/attachmentService'

const useGetAttachments = (meetingUuid) => {

    return useQuery({

        queryKey: ["attachments", meetingUuid],

        queryFn: () => getAttachments(meetingUuid),

        enabled: !!meetingUuid
    });
};

const useCreateAttachment = (meetingUuid) => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: createAttachment,

        onSuccess: () => {

            // Refresh only this meeting attachments
            client.invalidateQueries({
                queryKey: ["attachments", meetingUuid]
            });

            // Refresh attachmentCount in meeting
            client.invalidateQueries({
                queryKey: ["meeting", meetingUuid]
            });

            // Refresh attachmentCount in meeting list
            client.invalidateQueries({
                queryKey: ["meetings"]
            });

        }
    });
}

const useDeleteAttachment = (meetingUuid) => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: deleteAttachment,

        onSuccess: () => {

            client.invalidateQueries({
                queryKey: ["attachments", meetingUuid]
            });

            client.invalidateQueries({
                queryKey: ["meeting", meetingUuid]
            });

            client.invalidateQueries({
                queryKey: ["meetings"]
            });
        }
    });
};

export {

    useGetAttachments,
    useCreateAttachment,
    useDeleteAttachment
}
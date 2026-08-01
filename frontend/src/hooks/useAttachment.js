import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    getAttachments,
    createAttachment,
    deleteAttachment
} from '../services/attachmentService'

const useGetAttachments = (uuid) => {

    return useQuery({

        queryKey: ["attachments", uuid],

        queryFn: () => getAttachments(uuid),

        enabled: !!uuid
    });
};

const useCreateAttachment = (attachment) => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: createAttachment,

        onSuccess: () => {

            client.invalidateQueries({

                queryKey: ["attachments"]
            })
        }
    });
}

const useDeleteAttachment = () => {

    const client = useQueryClient();

    return useMutation({

        mutationFn: deleteAttachment,

        onSuccess: (_, uuid) => {

            client.invalidateQueries({
                queryKey: ["attachments"]
            });
        }
    });
};

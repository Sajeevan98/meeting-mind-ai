import apiClient from "../api/apiClient";

export const createAttachment = async (attachment) => {

    const response = await apiClient.post("/meeting/attachments", attachment)

    return response.data.data;
}

export const getAttachments = async (meeting_uuid) => {

    const response = await apiClient.get(`/meeting/attachments/${meeting_uuid}`)

    return response.data.data;
}

export const deleteAttachment = async (attachment_uuid) => {

    const response = await apiClient.delete(`/meeting/attachments/${attachment_uuid}`)

    return response.data.success;
}

import apiClient from "../api/apiClient";

export const createAttachment = async (attachment) => {

    const response = await apiClient.post("/attachments", attachment)

    return response.data.data;
}

export const getAttachments = async (meetingUuid) => {

    const response = await apiClient.get(`/attachments/meeting/${meetingUuid}`)

    return response.data.data;
}

export const deleteAttachment = async (attachmentUuid) => {

    const response = await apiClient.delete(`/attachments/${attachmentUuid}`)

    return response.data.success;
}

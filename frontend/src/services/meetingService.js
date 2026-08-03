import apiClient from "../api/apiClient"

export const createMeeting = async (meeting) => {

    const response = await apiClient.post("/meetings", meeting)

    return response.data.data;
}

export const getMeetings = async () => {

    const response = await apiClient.get("/meetings")

    return response.data.data;
}

export const getMeeting = async (meetingUuid) => {

    const response = await apiClient.get(`/meetings/${meetingUuid}`)

    return response.data.data;
}

export const updateMeeting = async (meetingUuid, meeting) => {

    const response = await apiClient.put(`/meetings/${meetingUuid}`, meeting)

    return response.data.data;
}

export const deleteMeeting = async (meetingUuid) => {

    const response = await apiClient.delete(`/meetings/${meetingUuid}`)

    return response.data.success;
}
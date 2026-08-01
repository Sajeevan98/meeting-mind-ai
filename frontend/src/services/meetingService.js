import apiClient from "../api/apiClient"

export const createMeeting = async (meeting) => {

    const response = await apiClient.post("/meeting", meeting)

    return response.data.data;
}

export const getMeetings = async () => {

    const response = await apiClient.get("/meeting")

    return response.data.data;
}

export const getMeeting = async (uuid) => {

    const response = await apiClient.get(`/meeting/${uuid}`)

    return response.data.data;
}

export const updateMeeting = async (uuid, meeting) => {

    const response = await apiClient.put(`/meeting/${uuid}`, meeting)

    return response.data.data;
}

export const deleteMeeting = async (uuid) => {

    const response = await apiClient.delete(`/meeting/${uuid}`)

    return response.data.success;
}
import apiClient from "../api/apiClient"

// analyze Meeting (meeting-uuid, request = [aiProvider, model])
export const createAnalysis = async (meetingUuid, request) => {

    const response = await apiClient.post(`/analyses/meeting/${meetingUuid}`, request)

    return response.data.data;
}

// get analysis by analysis-uuid
export const getAnalysis = async (analysisUuid) => {

    const response = await apiClient.get(`/analyses/${analysisUuid}`)

    return response.data.data;
}

// get analyses history for a meeting
export const getAnalyses = async (meetingUuid) => {

    const response = await apiClient.get(`/analyses/meeting/${meetingUuid}`)

    return response.data.data;
}

// remove analysis by analysis-uuid
export const deleteAnalysis = async (analysisUuid) => {

    const response = await apiClient.delete(`/analyses/${analysisUuid}`)

    return response.data.success;
}
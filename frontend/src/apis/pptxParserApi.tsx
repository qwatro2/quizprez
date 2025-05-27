import axios from "axios";

const API_BASE_URL = 'http://localhost:8088/api/v1';

export const uploadPptx = async (
    file: File
) => {
    const formData = new FormData();
    formData.append('file', file);

    const api = axios.create({
        baseURL: API_BASE_URL,
        timeout: 300000,
        headers: {
            'Content-Type': 'multipart/form-data',
            'accept': '*/*'
        },
    });

    const response = await api.post('/parse/pptx', formData);

    if (response.data) {
        return response.data;
    }
}
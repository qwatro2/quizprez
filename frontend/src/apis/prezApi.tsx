import axios from "axios";

const API_BASE_URL = 'http://localhost:8084/api/v1';

export const fetchPrezById = async (
    id: string
) => {
    const response = await axios.get(`${API_BASE_URL}/presentations/${id}`);
    console.log(response.data);
    return response.data;
}

export const fetchPrezs = async () => {
    const response = await axios.get(`${API_BASE_URL}/presentations`, {
        params: {
            ownerId: 0
        }
    });
    console.log(response.data);
    return response.data;
}

export const uploadPrez = async (
    title: string,
    html: string
) => {
    const response = await axios.post(`${API_BASE_URL}/presentations`, {
        ownerId: 0,
        title: title,
        html: html
    });
    console.log(response.data);
    return response.data;
}

export const updatePrez = async (
    id: number,
    ownerId: number,
    title: string,
    html: string
) => {
    const response = await axios.put(`${API_BASE_URL}/presentations/${id}`, {
        ownerId: ownerId,
        title: title,
        html: html
    });
    console.log(response.data);
    return response.data;
}
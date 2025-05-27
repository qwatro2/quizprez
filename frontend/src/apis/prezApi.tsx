import axios from "axios";

const API_BASE_URL = 'http://localhost:8084/api/v1';

export const fetchPrezById = async (
    id: string
)=> {
    const response = await axios.get(`${API_BASE_URL}/presentations/${id}`);
    console.log(response.data);
    return response.data;
}
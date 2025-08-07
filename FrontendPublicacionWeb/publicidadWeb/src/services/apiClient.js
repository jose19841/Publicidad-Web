import axios from "axios";

// Configuración base del cliente Axios
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080", // URL del backend
  withCredentials: true, 
  headers: {
    "Content-Type": "application/json",
  },
});



export default apiClient;

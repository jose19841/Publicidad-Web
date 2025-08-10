// src/modules/providers/services/getProviderService.js
import apiClient from "../../../services/apiClient";

export default async function getProviderService() {
  // El backend devuelve todos los proveedores en /api/providers/getAll (sin paginación ni get by id)
  const response = await apiClient.get("/api/providers/getAll");

  // Normalizamos a { data, total } para el DataTable
  const data = Array.isArray(response.data)
    ? response.data
    : (response.data?.items || []);

  const total = Array.isArray(response.data)
    ? data.length
    : (response.data?.total ?? data.length);

  return { data, total };
}

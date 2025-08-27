// src/modules/providers/services/deleteProviderService.js
import apiClient from "../../../services/apiClient";

export default async function deleteProviderService(id) {
  const response = await apiClient.delete(`/api/providers/disable/${id}`);
  return response.data;
}

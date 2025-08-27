// src/modules/providers/services/deleteProviderService.js
import apiClient from "../../../services/apiClient";

export default async function enableProviderService(id) {
  const response = await apiClient.put(`/api/providers/enable/${id}`);
  return response.data;
}

// src/modules/providers/services/editProviderService.js
import apiClient from "../../../services/apiClient";

export default async function editProviderService(id, data) {
  const response = await apiClient.put(`/api/providers/update/${id}`, data);
  return response.data;
}

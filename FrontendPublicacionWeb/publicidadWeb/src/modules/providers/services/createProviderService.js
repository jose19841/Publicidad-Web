// src/modules/providers/services/createProviderService.js
import apiClient from "../../../services/apiClient";

export default async function createProviderService(payload) {
  const { data } = await apiClient.post("/api/providers/create", payload);
  return data;
}

import apiClient from '../../../services/apiClient'

export async function getDashboardMetrics() {
  const response = await apiClient.get("/dashboard/metrics");
  return response.data;
}

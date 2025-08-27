import apiClient from '../../../services/apiClient'

export const getProviderRatings = async () => {
  const response = await apiClient.get("/providers/ratings");
  return response.data;
};

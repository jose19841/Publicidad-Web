import apiClient from '../../../services/apiClient';

export default async function createCategoryService(data) {
  const response = await apiClient.post('/api/categories', data);
  return response.data;
}

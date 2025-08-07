import apiClient from '../../../services/apiClient';

export default async function deleteCategoryService(id) {
  const response = await apiClient.delete(`/api/categories/${id}`);
  return response.data;
}

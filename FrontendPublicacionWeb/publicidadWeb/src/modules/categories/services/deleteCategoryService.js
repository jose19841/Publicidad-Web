import apiClient from '../../../services/apiClient';

export default async function deleteCategoryService(id) {
  const response = await apiClient.delete(`/categories/${id}`);
  return response.data;
}

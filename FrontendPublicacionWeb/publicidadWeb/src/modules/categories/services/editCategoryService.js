import apiClient from '../../../services/apiClient';

export default async function editCategoryService(id, data) {
  const response = await apiClient.put(`/categories/${id}`, data);
  return response.data;
}

import apiClient from '../../../services/apiClient';

export default async function getCategoriesService({ page = 1, pageSize = 10 } = {}) {
  const response = await apiClient.get(`/api/categories`, {
    params: { page, pageSize },
  });

  const data = response.data?.items || response.data || [];
  const total = response.data?.total || data.length;

  return {
    data,
    total,
  };
}

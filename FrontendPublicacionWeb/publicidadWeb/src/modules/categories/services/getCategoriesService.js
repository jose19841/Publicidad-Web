// src/modules/categories/services/getCategoriesService.js
import apiClient from '../../../services/apiClient';

export default async function getCategoriesService({ page = 1, pageSize = 10 } = {}) {
  // El backend devuelve TODAS las categorías sin paginar
  const response = await apiClient.get('/api/categories/getAll');

  const list = Array.isArray(response.data)
    ? response.data
    : (response.data?.items || []);

  const total = list.length;

  // Paginación client-side
  const start = (page - 1) * pageSize;
  const data = list.slice(start, start + pageSize);

  return { data, total };
}

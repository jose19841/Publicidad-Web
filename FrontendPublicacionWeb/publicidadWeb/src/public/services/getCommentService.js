// modules/comments/services/commentsService.js
import apiClient from "../../services/apiClient";


export async function getCommentsByProvider(providerId) {
  const { data } = await apiClient.get(`/comments/getAllByProvider`, { params: { providerId } });
  return data;
}

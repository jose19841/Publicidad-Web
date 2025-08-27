import apiClient from '../../../services/apiClient'

export const deleteComment = async (commentId) => {
  const response = await apiClient.delete(`/comments/delete/${commentId}`);
  return response.data;
};

export const getCommentsByProvider = async (providerId) => {
  const response = await apiClient.get(`/comments/getAllByProvider`, {
    params: { providerId },
  });
  return response.data;
};

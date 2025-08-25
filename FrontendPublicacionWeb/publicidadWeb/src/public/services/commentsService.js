// modules/comments/services/commentsService.js
import apiClient from "../../services/apiClient";

export async function createComment({ providerId, content }) {
  const { data } = await apiClient.post("/comments", { providerId, content });
  return data;
}



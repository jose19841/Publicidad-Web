// modules/comments/hooks/useComments.js
import { useCallback, useEffect, useState } from "react";
import { getCommentsByProvider } from "../services/getCommentService";

export default function useComments(providerId) {
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchComments = useCallback(async () => {
    if (!providerId) return;
    try {
      setLoading(true);
      setError(null);
      const data = await getCommentsByProvider(providerId);
      setComments(data);
    } catch (err) {
      setError(err?.response?.data?.message || err.message || "Error al cargar comentarios");
    } finally {
      setLoading(false);
    }
  }, [providerId]);

  useEffect(() => {
    fetchComments();
  }, [fetchComments]);

  return { comments, loading, error, refresh: fetchComments };
}

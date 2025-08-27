// modules/comments/hooks/useCreateComment.js
import { useState } from "react";
import { createComment } from "../services/commentsService";

export default function useCreateComment() {
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState(null);

  const send = async ({ providerId, content }) => {
    try {
      setLoading(true); setError(null);
      return await createComment({ providerId, content });
    } catch (e) {
      setError(e?.response?.data?.message || e.message || "No se pudo enviar el comentario");
      throw e;
    } finally { setLoading(false); }
  };

  return { send, loading, error };
}

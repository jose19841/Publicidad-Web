// modules/ratings/hooks/useRateProvider.js
import { useState } from "react";
import { createRating } from "../services/ratingsService";

export default function useRateProvider() {
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState(null);

  const rate = async ({ providerId, score }) => {
    try {
      setLoading(true); setError(null);
      return await createRating({ providerId, score });
    } catch (e) {
      setError(e?.response?.data?.message || e.message || "No se pudo calificar");
      throw e;
    } finally { setLoading(false); }
  };

  return { rate, loading, error };
}

// src/hooks/useProviderRatings.js
import { useState, useEffect } from "react";
import { getProviderRatings } from "../services/providerRatingsService";

export default function useProviderRatings() {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const data = await getProviderRatings();
        setRatings(data);
      } catch (err) {
        console.error("Error cargando calificaciones:", err);
        setError("Error al cargar calificaciones.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return { ratings, loading, error };
}

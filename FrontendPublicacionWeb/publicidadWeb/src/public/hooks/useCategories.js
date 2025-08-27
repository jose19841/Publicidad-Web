// src/hooks/useCategories.js
import { useCallback, useState } from "react";
import getCategoriesService from '../../modules/categories/services/getCategoriesService'

export default function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading]   = useState(false);
  const [error, setError]       = useState("");

  const fetchCategories = useCallback(async () => {
    try {
      setLoading(true);
      setError("");

      // Si tu servicio ya devuelve { data: [...] } o un array directo, adaptalo:
      const res = await getCategoriesService({ page: 1, pageSize: 1000 });
      const list = Array.isArray(res) ? res : res?.data || [];
      setCategories(list);
    } catch (e) {
      setError("No se pudieron cargar las categorías.");
    } finally {
      setLoading(false);
    }
  }, []);

  return { categories, loading, error, fetchCategories };
}

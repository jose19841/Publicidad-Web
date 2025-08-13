import { useCallback, useEffect, useState } from "react";
import { getAllProviders, searchProviders } from "../services/providerService";

export default function useProviders(initialName = "", initialCategory = "") {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [name, setName] = useState(initialName);
  const [category, setCategory] = useState(initialCategory);

  // Busca primero por nombre; si no hay resultados, busca por categoría
  const refetch = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      let results = [];

      // 1) Intento por nombre (si hay name)
      if (name?.trim()) {
        results = await searchProviders({ name: name.trim(), category: "" });
      }

      // 2) Si no hubo resultados, intento por categoría (usa el mismo texto)
      if ((!results || results.length === 0) && name?.trim()) {
        results = await searchProviders({ name: "", category: name.trim() });
      }

      // 3) Si explícitamente setearon category distinto del name, respetarlo
      if (
        (!results || results.length === 0) &&
        category?.trim() &&
        category.trim() !== name?.trim()
      ) {
        results = await searchProviders({
          name: "",
          category: category.trim(),
        });
      }

      setList(results || []);
    } catch {
      setError("No se pudo cargar la lista de prestadores.");
      setList([]);
    } finally {
      setLoading(false);
    }
  }, [name, category]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  const upsertProvider = useCallback((updated) => {
    if (!updated?.id) return;
    setList((prev) =>
      prev.some((p) => p.id === updated.id)
        ? prev.map((p) => (p.id === updated.id ? { ...p, ...updated } : p))
        : [updated, ...prev]
    );
  }, []);

  const updateProviderInList = (updated) => {
    if (!updated?.id) return;
    setList((prev) =>
      prev.map((p) => (p.id === updated.id ? { ...p, ...updated } : p))
    );
  };

  // Unificamos el input del buscador
  const setQuery = useCallback((value) => {
    setName(value);
    setCategory(""); // evitamos AND; si no hay resultados, refetch probará por categoría con name
  }, []);

  const fetchAllProviders = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const results = await getAllProviders(); // <-- llama a la función del service
      setList(results || []);
    } catch {
      setError("No se pudo cargar la lista de prestadores.");
      setList([]);
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    list,
    loading,
    error,
    name,
    setName,
    category,
    setCategory,
    refetch,
    upsertProvider,
    updateProviderInList,
    query: name,
    setQuery,
    fetchAllProviders,
  };
}

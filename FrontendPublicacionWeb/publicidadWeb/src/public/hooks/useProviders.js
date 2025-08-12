// modules/providers/hooks/useProviders.js
import { useEffect, useMemo, useState, useCallback } from "react";
import { getAllProviders } from "../services/providerService";

export default function useProviders(initialQuery = "") {
  const [all, setAll] = useState([]);
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [query, setQuery] = useState(initialQuery);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await getAllProviders();
      setAll(res || []);
    } catch (e) {
      setError("No se pudo cargar la lista de prestadores.");
    } finally {
      setLoading(false);
    }
  }, []);

  // ⬇️ upsert local (sin backend)
  const upsertProvider = useCallback((updated) => {
    if (!updated?.id) return;
    setAll(prev =>
      prev.some(p => p.id === updated.id)
        ? prev.map(p => (p.id === updated.id ? { ...p, ...updated } : p))
        : [updated, ...prev]
    );
  }, []);

  useEffect(() => { refetch(); }, [refetch]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return all;
    return all.filter(p => {
      const full = `${p.name ?? ""} ${p.lastName ?? ""}`.toLowerCase();
      return (
        full.includes(q) ||
        (p.categoryName ?? "").toLowerCase().includes(q) ||
        (p.description ?? "").toLowerCase().includes(q)
      );
    });
  }, [all, query]);

  useEffect(() => { setList(filtered); }, [filtered]);

  const updateProviderInList = (updated) => {
  if (!updated?.id) return;
  setAll(prev =>
    prev.map(p => (p.id === updated.id ? { ...p, ...updated } : p))
  );
};

  return { list, loading, error, setQuery, refetch, upsertProvider, updateProviderInList };
}

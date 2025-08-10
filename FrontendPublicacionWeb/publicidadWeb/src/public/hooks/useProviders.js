import { useEffect, useMemo, useState } from "react";
import { getAllProviders } from "../services/providerService";

export default function useProviders(initialQuery = "") {
  const [all, setAll] = useState([]);
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [query, setQuery] = useState(initialQuery);

  useEffect(() => {
    let ignore = false;
    (async () => {
      try {
        setLoading(true);
        setError("");
        const res = await getAllProviders();
        if (ignore) return;
        setAll(res || []);
      } catch (e) {
        setError("No se pudo cargar la lista de prestadores.");
      } finally {
        if (!ignore) setLoading(false);
      }
    })();
    return () => { ignore = true; };
  }, []);

  // Filtro en memoria por nombre, apellido, rubro y descripción
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

  return { list, loading, error, setQuery };
}

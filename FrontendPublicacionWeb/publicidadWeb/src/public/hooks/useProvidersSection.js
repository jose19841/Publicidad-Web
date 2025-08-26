import { useState, useEffect, useCallback } from "react";
import { getProvidersPaged } from "../services/providerService";

export default function useProvidersSection(items) {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(null);

  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [paginatedItems, setPaginatedItems] = useState(items || []);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  // ✅ mover arriba para usar en useEffect
  const fetchProviders = useCallback(async (pageToLoad) => {
    setLoading(true);
    try {
      const data = await getProvidersPaged(pageToLoad, size);

      if (pageToLoad === 0) {
        // primera página → reemplazo
        setPaginatedItems(data.content);
      } else {
        // cargar más → acumulo
        setPaginatedItems((prev) => [...prev, ...data.content]);
      }

      setPage(data.number);
      setTotalPages(data.totalPages);
    } catch (e) {
      console.error("Error cargando proveedores", e);
    } finally {
      setLoading(false);
    }
  }, [size]);

  // 🔑 Carga inicial
  useEffect(() => {
    if (!items || items.length === 0) {
      fetchProviders(0);
    } else {
      // si viene lista de búsqueda → usar esa
      setPaginatedItems(items);
    }
  }, [items, fetchProviders]);

  const handleLoadMore = useCallback(() => {
    if (page < totalPages - 1) {
      fetchProviders(page + 1);
    }
  }, [page, totalPages, fetchProviders]);

  const handleView = useCallback((provider) => {
    setSelected(provider);
    setOpen(true);
  }, []);

  const handleClose = useCallback(() => {
    setOpen(false);
    setSelected(null);
  }, []);

  const handleContact = useCallback((provider) => {
    setSelected(provider);
    setOpen(true);
  }, []);

  // ✅ corregido: arranca desde la página 0 en vez de llamar getAll
  const handleViewAll = useCallback(() => {
    fetchProviders(0);
  }, [fetchProviders]);

  return {
    open,
    selected,
    paginatedItems,
    loading,
    page,
    totalPages,
    handleLoadMore,
    handleView,
    handleClose,
    handleContact,
    handleViewAll,
  };
}

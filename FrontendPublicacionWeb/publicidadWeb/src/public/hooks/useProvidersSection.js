// src/components/hooks/useProvidersSection.js
import { useState, useEffect, useCallback } from "react";

export default function useProvidersSection(items, fetchAllProviders) {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(null);
  const [page, setPage] = useState(1);
  const pageSize = 4;

  // Items paginados
  const paginatedItems = items.slice(0, page * pageSize);

  // Resetear página cuando cambian los datos
  useEffect(() => {
    setPage(1);
  }, [items]);

  const handleLoadMore = useCallback(() => {
    setPage((prev) => prev + 1);
  }, []);

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

  const handleViewAll = useCallback(() => {
    fetchAllProviders();
    setPage(1);
  }, [fetchAllProviders]);

  return {
    open,
    selected,
    paginatedItems,
    handleLoadMore,
    handleView,
    handleClose,
    handleContact,
    handleViewAll,
  };
}

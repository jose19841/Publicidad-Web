// src/pages/hooks/useHomePage.js
import { useCallback, useEffect } from "react";
import useProviders from "./useProviders";
import { getProviderById } from "../services/providerService";

export default function useHomePage() {
  const {
    list,
    loading,
    error,
    setQuery,
    updateProviderInList,
    fetchAllProviders,
  } = useProviders();

  // Buscar por texto
  const onSearch = useCallback((value) => {
    setQuery(value);
  }, [setQuery]);

  // Refrescar un proveedor puntual desde backend y actualizar lista local
  const onProviderUpdated = useCallback(async (id) => {
    const fresh = await getProviderById(id);
    updateProviderInList(fresh);
  }, [updateProviderInList]);

  // Carga inicial
  useEffect(() => {
    fetchAllProviders();
  }, [fetchAllProviders]);

  return {
    list,
    loading,
    error,
    onSearch,
    onProviderUpdated,
    fetchAllProviders,
  };
}

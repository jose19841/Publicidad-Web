// src/modules/providers/hooks/useProvidersTable.js
import { useState, useEffect, useMemo, useCallback } from "react";
import Swal from "sweetalert2";
import getProviderService from "../services/getProviderService";
import deleteProviderService from "../services/deleteProviderService";
import editProviderService from "../services/editProviderService";

export default function useProvidersTable() {
  const [allData, setAllData] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProviders = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await getProviderService(); // GET /api/providers
      const list = Array.isArray(res) ? res : res?.data || [];
      setAllData(list);
    } catch (err) {
      setError(err?.message || "Error al cargar los prestadores");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProviders();
  }, [fetchProviders]);

  // Helpers de actualización local
  const markDisabledLocal = useCallback((id) => {
    setAllData((prev) => prev.map((p) => (p.id === id ? { ...p, isActive: false } : p)));
  }, []);

  const markEnabledLocal = useCallback((id) => {
    setAllData((prev) => prev.map((p) => (p.id === id ? { ...p, isActive: true } : p)));
  }, []);

  const total = allData.length;

  const paginatedData = useMemo(() => {
    const start = (page - 1) * pageSize;
    return allData.slice(start, start + pageSize);
  }, [allData, page, pageSize]);

  const handlePageChange = (newPage) => setPage(newPage);
  const handlePageSizeChange = (newSize) => {
    setPageSize(newSize);
    setPage(1);
  };

  // Inhabilitar
  const handleDisable = async (provider) => {
    const confirm = await Swal.fire({
      title: "¿Inhabilitar prestador?",
      text: `Se inhabilitará a ${provider.name} ${provider.lastName}.`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, inhabilitar",
      cancelButtonText: "Cancelar",
    });
    if (!confirm.isConfirmed) return;

    try {
      await deleteProviderService(provider.id); // 204 No Content
      markDisabledLocal(provider.id); // Reflejar en UI sin refetch
      Swal.fire("Inhabilitado", "El prestador ha sido inhabilitado.", "success");
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo inhabilitar el prestador.",
        "error"
      );
    }
  };

  // Habilitar (PUT con todo el provider + isActive: true) -> evita 409
  const handleEnable = async (provider) => {
    const confirm = await Swal.fire({
      title: "¿Habilitar prestador?",
      text: `Se habilitará a ${provider.name} ${provider.lastName}.`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Sí, habilitar",
      cancelButtonText: "Cancelar",
    });
    if (!confirm.isConfirmed) return;

    try {
      await editProviderService(provider.id, { ...provider, isActive: true }); // <-- FIX
      markEnabledLocal(provider.id); // Reflejar en UI sin refetch
      Swal.fire("Habilitado", "El prestador ha sido habilitado.", "success");
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo habilitar el prestador.",
        "error"
      );
    }
  };

  // Actualizar datos del proveedor
  const handleUpdate = async (id, payload) => {
    try {
      const updated = await editProviderService(id, payload);
      setAllData((prev) => prev.map((p) => (p.id === id ? { ...p, ...updated } : p)));
    } catch (err) {
      console.error("Error actualizando proveedor:", err);
      throw err;
    }
  };

  return {
    data: paginatedData,
    total,
    allData,
    page,
    pageSize,
    loading,
    error,
    handlePageChange,
    handlePageSizeChange,
    handleDisable,
    handleEnable,
    handleUpdate,
    refresh: fetchProviders,
  };
}

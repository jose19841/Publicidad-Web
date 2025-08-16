// src/modules/providers/hooks/useProvidersTable.js
import { useCallback, useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";
import disableProviderService from "../services/disableProviderService";
import editProviderService from "../services/editProviderService"; // solo si lo usás en handleUpdate
import enableProviderService from "../services/enableProviderService";
import getProviderService from "../services/getProviderService";

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
      await disableProviderService(provider.id); // DELETE /api/providers/disable/{id} -> 204
      markDisabledLocal(provider.id);
      Swal.fire("Inhabilitado", "El prestador ha sido inhabilitado.", "success");
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo inhabilitar el prestador.",
        "error"
      );
    }
  };

  // Habilitar (usa endpoint dedicado PUT /enable/{id})
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
      await enableProviderService(provider.id); // PUT /api/providers/enable/{id} -> 204
      markEnabledLocal(provider.id);
      Swal.fire("Habilitado", "El prestador ha sido habilitado.", "success");
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo habilitar el prestador.",
        "error"
      );
    }
  };

  // Actualizar datos del proveedor (sigue usando tu edit service)
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

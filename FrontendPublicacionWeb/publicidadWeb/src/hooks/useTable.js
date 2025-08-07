import { useState, useEffect, useCallback } from "react";
import Swal from "sweetalert2";
import deleteCategoryService from "../modules/categories/services/deleteCategoryService";


function shallowEqual(a, b) {
  try {
    return JSON.stringify(a) === JSON.stringify(b);
  } catch {
    return false;
  }
}

export default function useTable(fetchFn, initialPageSize = 10) {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize] = useState(initialPageSize);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetchFn({ page, pageSize });

      const shouldUpdateData = !shallowEqual(result.data, data);
      const shouldUpdateTotal = result.total !== total;

      if (shouldUpdateData) {
        setData(result.data || []);
      }

      if (shouldUpdateTotal) {
        setTotal(result.total || 0);
      }

    } catch (err) {
      setError(err.message || "Error al cargar datos");
    } finally {
      setLoading(false);
    }
  }, [fetchFn, page, pageSize, data, total]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handlePageChange = (nextPage) => {
    const totalPages = Math.ceil(total / pageSize);
    if (nextPage > 0 && nextPage <= totalPages) {
      setPage(nextPage);
    }
  };

  const refresh = () => {
    loadData();
  };

  const handleDelete = async (item) => {
    if (!item?.id) return;

    const result = await Swal.fire({
      title: `¿Eliminar "${item.name}"?`,
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    try {
      await deleteCategoryService(item.id);
      await Swal.fire("Eliminado", "La categoría fue eliminada correctamente.", "success");
      refresh();
    } catch (err) {
      await Swal.fire("Error", err.message || "No se pudo eliminar", "error");
    }
  };

  return {
    data,
    page,
    pageSize,
    total,
    loading,
    error,
    handlePageChange,
    setPage,
    refresh,
    setData,
    handleDelete,
  };
}

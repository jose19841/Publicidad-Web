// src/pages/ProviderRatingsPage.jsx
import React, { useState, useMemo } from "react";
import useProviderRatings from "../hooks/useProviderRatings";
import DataTable from "../../../components/DataTable";
import CommentsModal from "../components/CommentsModal";

const ProviderRatingsPage = () => {
  const { ratings, loading, error } = useProviderRatings();
  const [selectedProvider, setSelectedProvider] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [sortConfig, setSortConfig] = useState({ field: null, direction: "asc" });
  const [page, setPage] = useState(1);
  const pageSize = 10;

  const columns = [
    { field: "id", label: "ID" },
    { field: "name", label: "Nombre" },
    { field: "lastName", label: "Apellido" },
    { field: "category", label: "Categoría" },
    { field: "ratingsCount", label: "Cantidad de Calificaciones" },
    {
      field: "averageScore",
      label: "Promedio",
      render: (value) => value.toFixed(2),
    },
  ];

  const actions = [
    {
      label: "Ver comentarios",
      onClick: (item) => {
        setSelectedProvider(item.id);
        setShowModal(true);
      },
    },
  ];

  // 🔎 Filtrado
  const filtered = useMemo(() => {
    return ratings.filter((r) => {
      const text = `${r.name} ${r.lastName} ${r.category}`.toLowerCase();
      return text.includes(search.toLowerCase());
    });
  }, [ratings, search]);

  // ↕️ Ordenamiento
  const sorted = useMemo(() => {
    if (!sortConfig.field) return filtered;

    return [...filtered].sort((a, b) => {
      const valA = a[sortConfig.field];
      const valB = b[sortConfig.field];

      if (typeof valA === "string") {
        return sortConfig.direction === "asc"
          ? valA.localeCompare(valB)
          : valB.localeCompare(valA);
      }
      return sortConfig.direction === "asc" ? valA - valB : valB - valA;
    });
  }, [filtered, sortConfig]);

  // 📄 Paginación
  const total = sorted.length;
  const totalPages = Math.ceil(total / pageSize);
  const paginated = useMemo(() => {
    const startIndex = (page - 1) * pageSize;
    return sorted.slice(startIndex, startIndex + pageSize);
  }, [sorted, page, pageSize]);

  // Cambiar orden
  const handleSort = (field) => {
    setSortConfig((prev) => {
      if (prev.field === field) {
        return { field, direction: prev.direction === "asc" ? "desc" : "asc" };
      }
      return { field, direction: "asc" };
    });
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Calificaciones y Comentarios de Proveedores</h2>

      {/* 🔎 Input de búsqueda */}
      <div className="mb-3">
        <input
          type="text"
          className="form-control"
          placeholder="Buscar por nombre, apellido o categoría..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(1); // reset página cuando busco
          }}
        />
      </div>

      {loading && <p>Cargando calificaciones...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && (
        <DataTable
          columns={columns.map((col) => ({
            ...col,
            label: (
              <span
                style={{ cursor: "pointer" }}
                onClick={() => handleSort(col.field)}
              >
                {col.label}{" "}
                {sortConfig.field === col.field
                  ? sortConfig.direction === "asc"
                    ? "↑"
                    : "↓"
                  : ""}
              </span>
            ),
          }))}
          data={paginated}
          total={total}
          page={page}
          pageSize={pageSize}
          onPageChange={setPage} // ahora sí funciona
          actions={actions}
        />
      )}

      <CommentsModal
        providerId={selectedProvider}
        show={showModal}
        onClose={() => setShowModal(false)}
      />
    </div>
  );
};

export default ProviderRatingsPage;

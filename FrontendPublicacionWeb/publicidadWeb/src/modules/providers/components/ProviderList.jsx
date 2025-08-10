// src/modules/providers/components/ProviderList.jsx
import React from "react";
import DataTable from "../../../components/DataTable";
import useProvidersTable from "../hooks/useProvidersTable";
import useProviderModal from "../hooks/useProviderModal";
import ProviderModal from "./ProviderModal";

// Resuelve el nombre de categoría a partir de la fila (sin estados locales)
function resolveCategory(value, row) {
  if (value) return value;
  if (row?.categoryName) return row.categoryName;
  if (row?.CategoryName) return row.CategoryName;
  if (row?.category?.name) return row.category.name;
  if (row?.category?.categoryName) return row.category.categoryName;
  return <span className="text-muted">-</span>;
}

export default function ProviderList() {
  const table = useProvidersTable();
  const { modalOpen, selectedProvider, openModal, closeModal } = useProviderModal();

  const columns = [
    { field: "name", label: "Nombre" },
    { field: "lastName", label: "Apellido" },
    { field: "phone", label: "Teléfono" },
    {
      field: "categoryName",
      label: "Categoría",
      render: (value, row) => resolveCategory(value, row),
    },
    {
      field: "isActive",
      label: "Estado",
      render: (value) => (value ? "Activo" : "Inhabilitado"),
    },
    {
      field: "_actions",
      label: "Acciones",
      render: (_, row) => (
        <div className="dropdown text-center">
          <button
            className="btn btn-light btn-sm"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            title="Acciones"
          >
            <span style={{ fontSize: 18 }}>⋮</span>
          </button>
          <ul className="dropdown-menu dropdown-menu-end">
            <li>
              <button
                className="dropdown-item"
                onClick={() => openModal(row)}
              >
                Editar
              </button>
            </li>
            <li>
              {row.isActive ? (
                <button
                  className="dropdown-item text-danger"
                  onClick={() => table.handleDisable(row)}
                >
                  Inhabilitar prestador
                </button>
              ) : (
                <button
                  className="dropdown-item"
                  onClick={() => table.handleEnable(row)}
                >
                  Habilitar prestador
                </button>
              )}
            </li>
          </ul>
        </div>
      ),
    },
  ];

  if (table.loading) return <div>Cargando...</div>;
  if (table.error) return <div className="text-danger">Error: {table.error}</div>;

  return (
    <>
      <DataTable
        columns={columns}
        data={table.data}      // paginado del hook
        // No usamos el prop actions del DataTable para poder personalizar el menú por fila
        page={table.page}
        pageSize={table.pageSize}
        total={table.total}
        onPageChange={table.handlePageChange}
        onPageSizeChange={table.handlePageSizeChange}
      />

      <ProviderModal
        isOpen={modalOpen}
        provider={selectedProvider}
        onClose={closeModal}
        onSuccess={() => {
          table.refresh();
          closeModal();
        }}
      />
    </>
  );
}

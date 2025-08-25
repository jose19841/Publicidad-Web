import DataTable from "../../../components/DataTable";
import useCategoriesTable from "../hooks/useCategoriesTable";
import useCategoryModal from "../hooks/useCategoryModal";
import CategoryModal from "./CategoryModal";
import React, { useState } from "react";


export default function CategoryList() {
  const table = useCategoriesTable();
  const {
    modalOpen,
    selectedCategory,
    openModal,
    closeModal
  } = useCategoryModal();

  const [search, setSearch] = useState("");

  const columns = [
    { field: "name", label: "Nombre" },
    { field: "description", label: "Descripción" },
  ];

  const actions = [
    {
      label: "Editar",
      onClick: (category) => openModal(category),
    },
    {
      label: "Eliminar",
      variant: "danger",
      onClick: table.handleDelete,
    },
  ];

  if (table.loading) return <div>Cargando...</div>;
  if (table.error) return <div className="text-danger">Error: {table.error}</div>;

  // Filtrar datos según búsqueda (por nombre o descripción)
  const filteredData = table.data.filter(
    (cat) =>
      cat.name.toLowerCase().includes(search.toLowerCase()) ||
      cat.description?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <>
      <input
        className="form-control mb-3"
        placeholder="Buscar categoría..."
        value={search}
        onChange={e => setSearch(e.target.value)}
      />
      <DataTable
        columns={columns}
        data={filteredData}
        actions={actions}
        page={table.page}
        pageSize={table.pageSize}
        total={filteredData.length}
        onPageChange={table.handlePageChange}
      />
      <CategoryModal
        isOpen={modalOpen}
        category={selectedCategory}
        onClose={closeModal}
        onUpdate={table.refresh}
      />
    </>
  );
}

import React from "react";
import DataTable from "../../../components/DataTable";
import useCategoriesTable from "../hooks/useCategoriesTable";
import useCategoryModal from "../hooks/useCategoryModal";
import CategoryModal from "./CategoryModal";

export default function CategoryList() {
  const table = useCategoriesTable();
  const {
    modalOpen,
    selectedCategory,
    openModal,
    closeModal
  } = useCategoryModal();

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

  return (
    <>
      <DataTable
        columns={columns}
        data={table.data}
        actions={actions}
        page={table.page}
        pageSize={table.pageSize}
        total={table.total}
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

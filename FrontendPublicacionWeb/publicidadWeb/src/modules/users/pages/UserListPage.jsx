// src/modules/users/pages/UserListPage.jsx
import React, { useState } from "react";
import useUsersTable from "../hooks/useUsersTable";
import DataTable from "../../../components/DataTable";
import UserSearchBar from "../components/UserSearchBar";
import UserActionsModal from "../components/UserActionsModal";
import { updateUser } from "../services/userService";

export default function UserListPage() {
  const {
    users,
    loading,
    error,
    page,
    setPage,
    total,
    search,
    setSearch,
    handleDelete,
    refetch,
    pageSize,
  } = useUsersTable();

  const [modalOpen, setModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  const columns = [
    { field: "email", label: "Email" },
    { field: "role", label: "Rol" },
    { field: "enabled", label: "Estado", render: (value) => value ? "Activo" : "Inactivo" },
  ];

  const actions = [
    {
      label: "Editar",
      onClick: (user) => {
        setSelectedUser(user);
        setModalOpen(true);
      },
    },
    {
      label: "Eliminar",
      variant: "danger",
      onClick: handleDelete,
    },
  ];

  const handleModalClose = () => {
    setModalOpen(false);
    setSelectedUser(null);
  };

  const handleSave = async (form) => {
    try {
      await updateUser(form.id, form);
      handleModalClose();
      refetch();
    } catch {
      alert("Error al actualizar usuario");
    }
  };

  if (loading) return <div>Cargando usuarios...</div>;
  if (error) return <div className="text-danger">Error: {error}</div>;

  return (
    <div>
      <h1 className="mb-4">Usuarios del sistema</h1>
      <UserSearchBar search={search} setSearch={setSearch} />
      <DataTable
        columns={columns}
        data={users}
        actions={actions}
        page={page}
        pageSize={pageSize}
        total={total}
        onPageChange={setPage}
      />
      <UserActionsModal
        open={modalOpen}
        user={selectedUser}
        onClose={handleModalClose}
        onSave={handleSave}
      />
    </div>
  );
}

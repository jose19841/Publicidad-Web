import { useState } from "react";
import useUsersTable from "../hooks/useUsersTable";
import { updateUser } from "../services/userService";

export default function useUserListPage() {
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

  return {
    users,
    loading,
    error,
    page,
    setPage,
    total,
    search,
    setSearch,
    columns,
    actions,
    modalOpen,
    selectedUser,
    handleModalClose,
    handleSave,
    pageSize,
  };
}

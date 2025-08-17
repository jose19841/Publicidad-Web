// src/modules/users/hooks/useUsersTable.js
import { useState, useEffect } from "react";
import { getUsers, changeStatus } from "../services/userService";
import Swal from "sweetalert2";


export default function useUsersTable(pageSize = 10) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Estados para búsqueda y paginación
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);

  // Trae todos los usuarios al montar
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getUsers();
      setUsers(res.data || res); // Ajustá según tu backend
      setError(null);
    } catch (e) {
      setError(e.message || "Error al obtener usuarios");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
    // eslint-disable-next-line
  }, []);

  // Filtrado local por email y rol
  const filtered = users.filter(
    (u) =>
      u.email.toLowerCase().includes(search.toLowerCase()) ||
      (u.role && u.role.toLowerCase().includes(search.toLowerCase()))
  );

  // Paginado local
  const total = filtered.length;
  const totalPages = Math.ceil(total / pageSize);
  const pagedData = filtered.slice((page - 1) * pageSize, page * pageSize);

  // Si cambia búsqueda, resetea página
  useEffect(() => {
    setPage(1);
  }, [search]);

  // Cambiar estado del usuario (habilitado/inhabilitado)
const handleDelete = async (user) => {
  const result = await Swal.fire({
    title: "¿Estás seguro?",
    text: `Se cambiará el estado del usuario: ${user.email}`,
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Sí, confirmar",
    cancelButtonText: "Cancelar",
  });

  if (result.isConfirmed) {
    try {
      await changeStatus(user.id);
      fetchUsers();

      Swal.fire({
        title: "Hecho",
        text: "El estado del usuario fue actualizado correctamente",
        icon: "success",
        timer: 2000,
        showConfirmButton: false,
      });
    } catch (e) {
      Swal.fire({
        title: "Error",
        text: "No se pudo actualizar el estado del usuario",
        icon: "error",
      });
    }
  }
};

  return {
    users: pagedData,
    loading,
    error,
    page,
    setPage,
    total,
    search,
    setSearch,
    handleDelete,
    refetch: fetchUsers,
    pageSize,
  };
}

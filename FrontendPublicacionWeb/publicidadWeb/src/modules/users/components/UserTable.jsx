// src/modules/users/components/UserTable.jsx
import React from "react";

export default function UserTable({ users, onEdit, onDelete }) {
  if (!users?.length) return <div>No hay usuarios.</div>;

  return (
    <table className="table">
      <thead>
        <tr>
          <th>Email</th>
          <th>Rol</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {users.map((u) => (
          <tr key={u.id}>
            <td>{u.email}</td>
            <td>{u.role}</td>
            <td>{u.enabled ? "Activo" : "Inactivo"}</td>
            <td>
              <button className="btn btn-sm btn-primary me-2" onClick={() => onEdit(u)}>
                Editar
              </button>
              <button className="btn btn-sm btn-danger" onClick={() => onDelete(u)}>
                Eliminar
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

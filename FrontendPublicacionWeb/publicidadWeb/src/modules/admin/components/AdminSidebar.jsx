// src/components/admin/AdminSidebar.jsx
import React from "react";
import { useAuth } from "../../context/AuthContext";
import { FaUserCircle, FaSignOutAlt, FaEdit } from "react-icons/fa";

export default function AdminSidebar() {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = "/login"; // o navigate("/login")
  };

  return (
    <div className="d-flex flex-column bg-dark text-white vh-100 p-3" style={{ width: 250 }}>
      <h3 className="mb-4">Admin Panel</h3>

      {/* Links de navegación */}
      <nav className="flex-grow-1">
        <a href="/admin/dashboard" className="d-block mb-2 text-white text-decoration-none">
          Dashboard
        </a>
        <a href="/admin/providers" className="d-block mb-2 text-white text-decoration-none">
          Prestadores
        </a>
        <a href="/admin/categories" className="d-block mb-2 text-white text-decoration-none">
          Categorías
        </a>
      </nav>

      {/* Menú de usuario */}
      <div className="mt-auto border-top pt-3">
        <div className="dropdown">
          <button
            className="btn btn-outline-light dropdown-toggle w-100 d-flex align-items-center gap-2"
            data-bs-toggle="dropdown"
          >
            <FaUserCircle size={20} />
            <span>{user?.name || "Administrador"}</span>
          </button>
          <ul className="dropdown-menu dropdown-menu-dark w-100">
            <li>
              <a className="dropdown-item" href="/admin/perfil">
                <FaEdit /> Editar perfil
              </a>
            </li>
            <li>
              <button className="dropdown-item" onClick={handleLogout}>
                <FaSignOutAlt /> Cerrar sesión
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

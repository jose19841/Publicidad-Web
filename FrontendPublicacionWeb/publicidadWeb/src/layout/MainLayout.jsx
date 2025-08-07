import React from "react";
import { Link, useLocation } from "react-router-dom";

export default function MainLayout({ children }) {
  const location = useLocation();

  return (
    <div className="d-flex" style={{ minHeight: "100vh" }}>
      {/* Sidebar */}
      <aside className="bg-dark text-white p-3" style={{ width: 250 }}>
        <h2 className="fs-4 mb-4 text-center">Panel Admin</h2>
        <ul className="nav flex-column">
          {/* Menú principal: Categorías */}
          <li className="nav-item mb-2">
            <span className="fw-bold">Categorías</span>
            <ul className="nav flex-column ms-3">
              <li>
                <Link
                  to="/categorias/crear"
                  className={`nav-link text-white${location.pathname === "/categorias/crear" ? " active bg-primary rounded" : ""}`}
                >
                  Crear nueva categoría
                </Link>
              </li>
              <li>
                <Link
                  to="/categorias"
                  className={`nav-link text-white${location.pathname === "/categorias" ? " active bg-primary rounded" : ""}`}
                >
                  Ver categorías
                </Link>
              </li>
            </ul>
          </li>
        </ul>
      </aside>

      {/* Contenido */}
      <main className="flex-grow-1 bg-light p-4" style={{ minHeight: "100vh" }}>
        {children}
      </main>
    </div>
  );
}

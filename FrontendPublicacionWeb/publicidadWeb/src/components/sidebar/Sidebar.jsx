import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { sidebarSections } from "./sidebarMenu";
import { FaChevronDown, FaChevronRight, FaUserCircle, FaSignOutAlt, FaEdit } from "react-icons/fa";
import { useAuth } from "../../context/AuthContext"; // <- tu contexto de auth

export default function Sidebar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [openIndex, setOpenIndex] = useState(null);

  const isActivePath = (path) => location.pathname === path;
  const hasActiveChild = (children) => children.some((child) => isActivePath(child.path));
  const handleToggle = (index) => {
    setOpenIndex((prev) => (prev === index ? null : index));
  };

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <aside className="bg-dark text-white p-3 d-flex flex-column" style={{ width: 250, minHeight: "100vh" }}>
      <h2 className="fs-4 mb-4 text-center">Panel Admin</h2>

      {/* Menú principal */}
      <ul className="nav flex-column flex-grow-1">
        {sidebarSections.map((section, idx) =>
          section.children ? (
            <li className="nav-item mb-2" key={section.title}>
              <button
                className="nav-link text-white d-flex align-items-center w-100 bg-transparent border-0 px-0"
                style={{ outline: "none", boxShadow: "none" }}
                onClick={() => handleToggle(idx)}
                aria-expanded={openIndex === idx || hasActiveChild(section.children)}
              >
                <span className="me-2">{section.icon}</span>
                <span className="flex-grow-1 text-start">{section.title}</span>
                {openIndex === idx || hasActiveChild(section.children) ? <FaChevronDown /> : <FaChevronRight />}
              </button>
              <ul
                className={`nav flex-column ms-3 collapse${
                  openIndex === idx || hasActiveChild(section.children) ? " show" : ""
                }`}
                style={{ transition: "height 0.2s" }}
              >
                {section.children.map((child) => (
                  <li className="nav-item mb-1" key={child.path}>
                    <Link
                      to={child.path}
                      className={`nav-link d-flex align-items-center text-white${
                        isActivePath(child.path) ? " active bg-primary rounded" : ""
                      }`}
                      style={{ fontSize: "0.97em" }}
                    >
                      <span className="me-2">{child.icon}</span>
                      {child.title}
                    </Link>
                  </li>
                ))}
              </ul>
            </li>
          ) : (
            <li className="nav-item mb-2" key={section.path}>
              <Link
                to={section.path}
                className={`nav-link d-flex align-items-center text-white${
                  isActivePath(section.path) ? " active bg-primary rounded" : ""
                }`}
              >
                <span className="me-2">{section.icon}</span>
                {section.title}
              </Link>
            </li>
          )
        )}
      </ul>

      {/* Sección de usuario */}
      <div className="border-top pt-3 mt-3">
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
              <Link className="dropdown-item" to="/admin/perfil">
                <FaEdit /> Editar perfil
              </Link>
            </li>
            <li>
              <button className="dropdown-item" onClick={handleLogout}>
                <FaSignOutAlt /> Cerrar sesión
              </button>
            </li>
          </ul>
        </div>
      </div>
    </aside>
  );
}

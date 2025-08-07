import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { sidebarSections } from "./sidebarMenu";
import { FaChevronDown, FaChevronRight } from "react-icons/fa";

export default function Sidebar() {
  const location = useLocation();
  const [openIndex, setOpenIndex] = useState(null);

  // Devuelve true solo si la ruta es exactamente igual
  const isActivePath = (path) => location.pathname === path;

  // Devuelve true si algún hijo está activo (para abrir el submenú)
  const hasActiveChild = (children) =>
    children.some((child) => isActivePath(child.path));

  // Permite que solo un submenú se expanda a la vez
  const handleToggle = (index) => {
    setOpenIndex((prev) => (prev === index ? null : index));
  };

  return (
    <aside className="bg-dark text-white p-3" style={{ width: 250 }}>
      <h2 className="fs-4 mb-4 text-center">Panel Admin</h2>
      <ul className="nav flex-column">
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
                {openIndex === idx || hasActiveChild(section.children) ? (
                  <FaChevronDown />
                ) : (
                  <FaChevronRight />
                )}
              </button>
              <ul
                className={`nav flex-column ms-3 collapse${
                  openIndex === idx || hasActiveChild(section.children)
                    ? " show"
                    : ""
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
    </aside>
  );
}

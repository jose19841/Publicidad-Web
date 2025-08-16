// src/components/header/HeaderDesktopActions.jsx
import { NavLink } from "react-router-dom";

export default function HeaderDesktopActions({
  user,
  menuUserOpen,
  toggleUserMenu,
  handleLogout,
}) {
  return (
    <div className="actions-desktop">
      {!user ? (
        <>
          <NavLink to="/login" className="ct-btn">Iniciar sesión</NavLink>
          <NavLink to="/register" className="ct-btn primary">Registrarse</NavLink>
        </>
      ) : (
        <div className="user-menu-wrapper">
          <button
            className="ct-btn"
            aria-haspopup="menu"
            aria-expanded={menuUserOpen}
            onClick={toggleUserMenu}
          >
            {user.username ?? "Mi cuenta"}
          </button>

          {menuUserOpen && (
            <ul role="menu" className="user-menu">
              <li>
                <NavLink to="/profile" role="menuitem" className="user-menu-link">
                  Mi perfil
                </NavLink>
              </li>
              <li>
                <button
                  role="menuitem"
                  onClick={handleLogout}
                  className="ct-btn user-menu-logout"
                >
                  Cerrar sesión
                </button>
              </li>
            </ul>
          )}
        </div>
      )}
    </div>
  );
}

// src/components/header/HeaderMobileDrawer.jsx
import { NavLink } from "react-router-dom";

export default function HeaderMobileDrawer({ open, user, handleLogout }) {
  return (
    <div id="menu-mobile" hidden={!open} className="mobile-menu">
      <div className="ct-container mobile-menu-container">
        <a href="#como-funciona">Cómo funciona</a>
        <a href="#contacto">Contacto</a>

        {!user ? (
          <>
            <NavLink to="/login" className="ct-btn">Iniciar sesión</NavLink>
            <NavLink to="/register" className="ct-btn primary">Registrarse</NavLink>
          </>
        ) : (
          <>
            <NavLink to="/profile" className="ct-btn">Mi perfil</NavLink>
            <button onClick={handleLogout} className="ct-btn">Cerrar sesión</button>
          </>
        )}
      </div>
    </div>
  );
}

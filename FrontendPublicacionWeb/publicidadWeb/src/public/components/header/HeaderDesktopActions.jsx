// src/components/header/HeaderDesktopActions.jsx
export default function HeaderDesktopActions({
  user,
  menuUserOpen,
  toggleUserMenu,
  handleLogout,
  onOpenProfile,
}) {
  return (
    <div className="actions-desktop">
      {!user ? (
        <>
          <a href="/login" className="ct-btn">Iniciar sesión</a>
          <a href="/register" className="ct-btn primary">Registrarse</a>
        </>
      ) : (
        // ancho fijo para que el drop y el disparador coincidan
        <div className="user-menu-wrapper" style={{ width: 260 }}>
          {/* disparador con el nombre (mismo ancho/alto) */}
          <button
            type="button"
            className="ct-btn d-block w-100 text-center py-2"
            aria-haspopup="menu"
            aria-expanded={menuUserOpen}
            onClick={toggleUserMenu}
          >
            {user.username ?? "Mi cuenta"}
          </button>

          {menuUserOpen && (
            <ul role="menu" className="user-menu list-unstyled mt-2 mb-0 p-0 w-100 text-center">
              {/* email (solo mostrar) — mismo tamaño */}
              <li className="mb-2">
                <a
                  href="#"
                  role="menuitem"
                  className="user-menu-link d-block w-100 text-center py-2 rounded-3 text-truncate"
                  onClick={(e) => e.preventDefault()}
                  aria-disabled="true"
                >
                  {user?.email}
                </a>
              </li>

              {/* editar perfil — mismo tamaño */}
              <li className="mb-2">
                <a
                  href="#"
                  role="menuitem"
                  className="user-menu-link d-block w-100 text-center py-2 rounded-3"
                  onClick={(e) => {
                    e.preventDefault();
                    toggleUserMenu();     // cerrar menú
                    onOpenProfile?.();    // abrir modal (siguiente paso)
                  }}
                >
                  Editar perfil
                </a>
              </li>

              {/* cerrar sesión — forzado rojo (Bootstrap danger) sin cambiar tus clases */}
              <li >
                <a
                  href="#"
                  role="menuitem"
                  className="ct-btn user-menu-logout d-block w-100 text-center py-2 rounded-3"
                  onClick={(e) => {
                    e.preventDefault();
                    handleLogout();
                  }}
                  style={{
                    backgroundColor: "var(--bs-danger)",  
                    color: "#fff"
                  }}
                >
                  Cerrar sesión
                </a>
              </li>
            </ul>
          )}
        </div>
      )}
    </div>
  );
}

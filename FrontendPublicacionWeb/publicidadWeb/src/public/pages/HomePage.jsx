import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from '../../context/AuthContext'

export default function HomePage() {
  const { user, logout } = useAuth();
  console.log(user)
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div style={{ padding: 32 }}>
      <h1>Página de Inicio</h1>
      <p>Bienvenido al sitio de clasificados.</p>
      <div>
        {user ? (
          <button onClick={handleLogout}>Cerrar sesión</button>
        ) : (
          <>
            <Link to="/login">Iniciar sesión</Link> |{" "}
            <Link to="/register">Registrarse</Link>
          </>
        )}
      </div>
    </div>
  );
}

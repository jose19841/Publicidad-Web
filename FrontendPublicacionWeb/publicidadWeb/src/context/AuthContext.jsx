import React, { createContext, useContext, useState, useEffect } from "react";
import authService from "../modules/users/services/authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Al montar o refrescar la app, consulta el perfil (cookie)
  useEffect(() => {
    let isMounted = true;
    authService
      .getProfile()
      .then((data) => {
        if (isMounted) setUser(data);
      })
      .catch(() => {
        if (isMounted) setUser(null);
      })
      .finally(() => {
        if (isMounted) setLoading(false);
      });
    return () => {
      isMounted = false;
    };
  }, []);

  // Guarda el user recibido del backend después del login
  const login = (userData) => setUser(userData);

  const logout = async () => {
    try {
      await authService.logout(); // Llama al backend para borrar la cookie
    } catch (error) {
      // Opcional: mostrar algún error si el backend falla
      console.error("Error al cerrar sesión:", error);
    }
    setUser(null); // Limpia el usuario del contexto SIEMPRE, incluso si el backend falla
  };

  
  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook para consumir fácil el contexto
export const useAuth = () => useContext(AuthContext);

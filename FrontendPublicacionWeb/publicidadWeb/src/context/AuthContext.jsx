// modules/users/context/AuthContext.jsx
import React, { createContext, useContext, useState, useEffect, useCallback } from "react";
import authService from "../modules/users/services/authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser]       = useState(null);
  const [loading, setLoading] = useState(true);

  // 🔹 NUEVO: control del modal de login y promesa pendiente
  const [loginOpen, setLoginOpen]   = useState(false);
  const [authResolver, setAuthResolver] = useState(null);

  // Al montar o refrescar la app, consulta el perfil (cookie)
  useEffect(() => {
    let isMounted = true;
    authService
      .getProfile()
      .then((data) => { if (isMounted) setUser(data); })
      .catch(() => { if (isMounted) setUser(null); })
      .finally(() => { if (isMounted) setLoading(false); });
    return () => { isMounted = false; };
  }, []);

  // Guarda el user recibido del backend después del login
  const login = (userData) => {
    setUser(userData);
    // 🔹 NUEVO: si venía de requireAuth, resolvemos y cerramos modal
    if (authResolver?.resolve) {
      authResolver.resolve(true);
      setAuthResolver(null);
    }
    setLoginOpen(false);
  };

  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error("Error al cerrar sesión:", error);
    }
    setUser(null);
  };

  // 🔹 NUEVO: si no hay sesión, abre el portal y espera al login
  const requireAuth = useCallback(() => {
    if (user) return Promise.resolve(true);
    setLoginOpen(true);
    return new Promise((resolve, reject) => setAuthResolver({ resolve, reject }));
  }, [user]);

  const cancelAuth = useCallback(() => {
  authResolver?.reject?.(new Error("login-cancelled"));
  setAuthResolver(null);
  setLoginOpen(false);
}, [authResolver]);

  return (
   <AuthContext.Provider value={{ user, loading, login, logout, requireAuth, loginOpen, setLoginOpen, cancelAuth }}>

      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

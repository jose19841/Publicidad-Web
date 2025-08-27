// src/hooks/useHeader.js
import { useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function useHeader() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);          // drawer mobile
  const [menuUserOpen, setMenuUserOpen] = useState(false); // menú usuario

  const toggleDrawer = useCallback(() => setOpen(o => !o), []);
  const toggleUserMenu = useCallback(() => setMenuUserOpen(v => !v), []);
  const closeUserMenu = useCallback(() => setMenuUserOpen(false), []);
  const handleLogout = useCallback(() => {
    logout();
    navigate("/");
  }, [logout, navigate]);

  return {
    user,
    open,
    menuUserOpen,
    toggleDrawer,
    toggleUserMenu,
    closeUserMenu,
    handleLogout,
  };
}

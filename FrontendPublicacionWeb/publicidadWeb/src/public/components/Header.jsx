// src/components/Header.jsx
import { useState } from "react";
import useHeader from "../hooks/useHeader";
import "../styles/Header.css";
import "../styles/theme.css";
import HeaderDesktopActions from "./header/HeaderDesktopActions";
import HeaderDesktopNav from "./header/HeaderDesktopNav";
import HeaderLogo from "./header/HeaderLogo";
import HeaderMobileButton from "./header/HeaderMobileButton";
import HeaderMobileDrawer from "./header/HeaderMobileDrawer";

import Swal from "sweetalert2";
import { useAuth } from "../../context/AuthContext";
import { getUserSession } from "../../modules/users/services/adminService.js";
import { updateLoggedUserUsername } from "../../modules/users/services/profileService.js";

import ChangePasswordModal from "/src/modules/users/components/ChangePasswordModal.jsx";
import UserProfileModal from "/src/modules/users/components/UserProfileModal.jsx";

export default function Header() {
  const {
    user,
    open,
    menuUserOpen,
    toggleDrawer,
    toggleUserMenu,
    handleLogout,
  } = useHeader();

  const { login } = useAuth();

  const [showUserProfile, setShowUserProfile] = useState(false);
  const [showChangePwd, setShowChangePwd] = useState(false);

  const handleUserProfileSubmit = async ({ username }) => {
    const trimmed = (username ?? "").trim();
    if (trimmed.length < 4 || trimmed.length > 20) {
      await Swal.fire({
        icon: "warning",
        title: "Nombre inválido",
        text: "Debe tener entre 4 y 20 caracteres.",
      });
      return;
    }

    try {
      await updateLoggedUserUsername(trimmed);
      try {
        const fresh = await getUserSession();
        login(fresh);
      } catch {
        // ignorar error de refresco
      }
      await Swal.fire({
        icon: "success",
        title: "Listo",
        text: "Nombre actualizado.",
        timer: 1400,
        showConfirmButton: false,
      });
      setShowUserProfile(false);
    } catch (err) {
      const msg =
        err?.response?.data?.message ||
        err?.response?.data ||
        "No se pudo actualizar el nombre.";
      await Swal.fire({ icon: "error", title: "Error", text: String(msg) });
    }
  };

  return (
    <header className="header">
      <div className="ct-container header-container">
        <HeaderLogo />
        <HeaderDesktopNav />
        <HeaderDesktopActions
          user={user}
          menuUserOpen={menuUserOpen}
          toggleUserMenu={toggleUserMenu}
          handleLogout={handleLogout}
          onOpenProfile={() => setShowUserProfile(true)}
        />
        <HeaderMobileButton open={open} toggleDrawer={toggleDrawer} />
      </div>

      <HeaderMobileDrawer open={open} user={user} handleLogout={handleLogout} />

      <UserProfileModal
        show={showUserProfile}
        onClose={() => setShowUserProfile(false)}
        user={user}
        onSubmit={handleUserProfileSubmit}
        onOpenChangePassword={() => setShowChangePwd(true)}
      />

      <ChangePasswordModal
        show={showChangePwd}
        onClose={() => setShowChangePwd(false)}
        onSubmit={() => setShowChangePwd(false)}
      />
    </header>
  );
}

// src/components/Header.jsx
import "../styles/theme.css";
import "../styles/Header.css";
import useHeader from "../hooks/useHeader";
import HeaderLogo from "./header/HeaderLogo";
import HeaderDesktopNav from "./header/HeaderDesktopNav";
import HeaderDesktopActions from "./header/HeaderDesktopActions";
import HeaderMobileButton from "./header/HeaderMobileButton";
import HeaderMobileDrawer from "./header/HeaderMobileDrawer";

export default function Header() {
  const {
    user,
    open,
    menuUserOpen,
    toggleDrawer,
    toggleUserMenu,
    handleLogout,
  } = useHeader();

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
        />
        <HeaderMobileButton open={open} toggleDrawer={toggleDrawer} />
      </div>

      <HeaderMobileDrawer open={open} user={user} handleLogout={handleLogout} />
    </header>
  );
}

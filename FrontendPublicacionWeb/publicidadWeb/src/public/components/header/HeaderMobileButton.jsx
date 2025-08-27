// src/components/header/HeaderMobileButton.jsx
export default function HeaderMobileButton({ open, toggleDrawer }) {
  return (
    <button
      className="ct-btn mobile-menu-btn"
      onClick={toggleDrawer}
      aria-expanded={open}
      aria-controls="menu-mobile"
      aria-label="Abrir menú"
    >
      ☰
    </button>
  );
}

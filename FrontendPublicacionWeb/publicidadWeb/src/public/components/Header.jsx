import React, { useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import "../styles/theme.css";

export default function Header(){
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [menuUserOpen, setMenuUserOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header style={{ background: "var(--ct-bg)", borderBottom: "1px solid var(--ct-border)" }}>
      <div className="ct-container" style={{ display:"flex", alignItems:"center", justifyContent:"space-between", height:64 }}>
        {/* Logo */}
        <Link to="/" style={{ display:"flex", alignItems:"center", gap:10, textDecoration:"none" }}>
          <div style={{ width:34, height:34, borderRadius:8, background:"var(--ct-primary)" }} />
          <span style={{ color:"var(--ct-text)", fontWeight:800, letterSpacing:0.2 }}>ClasifiTrenque</span>
        </Link>

        {/* Desktop nav */}
        <nav aria-label="Principal" className="nav-desktop" style={{ display:"none" }}>
          <ul style={{ display:"flex", gap:20, listStyle:"none", margin:0, padding:0 }}>
            <li><a href="#como-funciona" style={{ color:"var(--ct-text)", textDecoration:"none" }}>Cómo funciona</a></li>
            <li><a href="#contacto" style={{ color:"var(--ct-text)", textDecoration:"none" }}>Contacto</a></li>
          </ul>
        </nav>

        {/* Actions */}
        <div className="actions-desktop" style={{ display:"none", alignItems:"center", gap:12 }}>
          {!user ? (
            <>
              <NavLink to="/login" className="ct-btn">Iniciar sesión</NavLink>
              <NavLink to="/register" className="ct-btn primary">Registrarse</NavLink>
            </>
          ) : (
            <div style={{ position:"relative" }}>
              <button
                className="ct-btn"
                aria-haspopup="menu"
                aria-expanded={menuUserOpen}
                onClick={()=>setMenuUserOpen(v=>!v)}
              >
                {user.username ?? "Mi cuenta"}
              </button>
              {menuUserOpen && (
                <ul role="menu" style={{
                  position:"absolute", right:0, top:"110%",
                  background:"var(--ct-surface)", border:"1px solid var(--ct-border)",
                  borderRadius:12, padding:8, listStyle:"none", margin:0, minWidth:180
                }}>
                  <li><NavLink to="/profile" role="menuitem" style={{ display:"block", padding:"8px 10px", color:"var(--ct-text)", textDecoration:"none" }}>Mi perfil</NavLink></li>
                  <li><button role="menuitem" onClick={handleLogout} className="ct-btn" style={{ width:"100%", marginTop:6 }}>Cerrar sesión</button></li>
                </ul>
              )}
            </div>
          )}
        </div>

        {/* Mobile menu button */}
        <button
          className="ct-btn"
          onClick={()=>setOpen(o=>!o)}
          aria-expanded={open}
          aria-controls="menu-mobile"
          aria-label="Abrir menú"
          style={{ display:"inline-flex" }}
        >
          ☰
        </button>
      </div>

      {/* Mobile drawer */}
      <div id="menu-mobile" hidden={!open} style={{ borderTop:"1px solid var(--ct-border)", background:"var(--ct-surface)" }}>
        <div className="ct-container" style={{ display:"flex", flexDirection:"column", gap:12, padding:"14px 0" }}>
          <a href="#como-funciona" style={{ color:"var(--ct-text)", textDecoration:"none" }}>Cómo funciona</a>
          <a href="#contacto" style={{ color:"var(--ct-text)", textDecoration:"none" }}>Contacto</a>
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

      {/* Responsive helpers */}
      <style>{`
        @media (min-width: 960px){
          .nav-desktop, .actions-desktop { display:flex !important; }
          header > .ct-container > button[aria-label="Abrir menú"] { display:none !important; }
        }
        header a:hover { text-decoration: underline; text-decoration-color: var(--ct-muted); }
      `}</style>
    </header>
  );
}

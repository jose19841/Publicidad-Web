// ProviderModal/Header.jsx
import React from "react";
export default function Header({ provider, onClose }) {
  const { name, lastName, categoryName, photoUrl } = provider;
  const fullName = [name, lastName].filter(Boolean).join(" ");
  return (
    <div style={{ display:"flex", alignItems:"center", gap:12, background:"var(--ct-surface)", borderBottom:"1px solid var(--ct-border)", padding:14 }}>
      <div style={{ width:56, height:56, borderRadius:12, overflow:"hidden", background:"#0b1b2b", display:"grid", placeItems:"center" }}>
        {photoUrl ? <img src={photoUrl} alt="" style={{ width:"100%", height:"100%", objectFit:"cover" }} /> : <span style={{ color:"var(--ct-muted)", fontWeight:700 }}>CT</span>}
      </div>
      <div style={{ flex:1, minWidth:0 }}>
        <h3 style={{ margin:0, color:"var(--ct-text)" }}>{fullName || "Prestador"}</h3>
        <p style={{ margin:0, color:"var(--ct-muted)" }}>{categoryName ?? "—"}</p>
      </div>
      <button className="ct-btn" onClick={onClose}>Cerrar</button>
    </div>
  );
}

// ProviderModal/Pagination.jsx
import React from "react";
export default function Pagination({ page, onPrev, onNext }) {
  return (
    <div style={{ display:"flex", justifyContent:"flex-end", gap:8 }}>
      <button className="ct-btn" onClick={onPrev} disabled={page===1}>Anterior</button>
      <button className="ct-btn primary" onClick={onNext}>Siguiente</button>
    </div>
  );
}

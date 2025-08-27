// ProviderModal/Pagination.jsx
import React from "react";
export default function Pagination({ page, onPrev, onNext,totalPages }) {
  return (
    <div style={{ display:"flex", justifyContent:"flex-end", gap:8 }}>
      <button className="ct-btn" onClick={onPrev} disabled={page <= 1}>Anterior</button>
        <span className="text-center text-white pt-2">Página {page} de {totalPages}</span>
      <button className="ct-btn primary" onClick={onNext} disabled={page >= totalPages}>Siguiente</button>
    </div>
  );
}

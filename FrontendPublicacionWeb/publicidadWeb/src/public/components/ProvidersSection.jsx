// ProvidersSection.jsx (fragmento de integración)
import React, { useState } from "react";
import "../styles/theme.css";
import ProviderCard from "./ProviderCard";
import ProviderModal from "../components/providerModal/ProviderModal";

export default function ProvidersSection({ title = "Prestadores", items = [], loading, onLoadMore, hasMore,   onProviderUpdated }) {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(null);

  const handleView = (p) => { setSelected(p); setOpen(true); };
  const handleClose = () => { setOpen(false); setSelected(null); };
  const handleContact = (p) => { setSelected(p); setOpen(true); }; // o acción directa a WhatsApp si querés


  
  return (
    <section aria-labelledby="providers-title" style={{ padding: "28px 0", background: "var(--ct-bg)" }}>
      <div className="ct-container" style={{ display: "grid", gap: 16 }}>
        <h2 id="providers-title" style={{ margin: 0, color: "var(--ct-text)", fontSize: 20 }}>{title}</h2>

        {/* Grid */}
        <div role="list" style={{ display: "grid", gap: 12, gridTemplateColumns: "1fr" }}>
          <style>{`
            @media (min-width: 560px){ [role="list"]{ grid-template-columns: repeat(2, 1fr); } }
            @media (min-width: 960px){ [role="list"]{ grid-template-columns: repeat(3, 1fr); } }
            @media (min-width: 1280px){ [role="list"]{ grid-template-columns: repeat(4, 1fr); } }
          `}</style>

          {loading && items.length === 0 &&
            Array.from({ length: 6 }).map((_, i) => (
              <div key={i} aria-hidden="true" style={{
                background: "var(--ct-surface)", border: "1px solid var(--ct-border)",
                borderRadius: 14, height: 116, animation: "pulse 1.4s ease-in-out infinite"
              }} />
            ))
          }

          {!loading && items.length === 0 && (
            <div role="status" style={{
              border: "1px dashed var(--ct-border)", borderRadius: 14,
              padding: 16, color: "var(--ct-muted)"
            }}>
              No se encontraron prestadores. Probá con otro término (ej: “electricista”, “pintor”).
            </div>
          )}

          {items.map((p) => (
            <div role="listitem" key={p.id}>
              <ProviderCard provider={p} onView={handleView} onContact={handleContact} />
            </div>
          ))}
        </div>

        {(hasMore || loading) && (
          <div style={{ display: "flex", justifyContent: "center", paddingTop: 8 }}>
            <button className="ct-btn" onClick={onLoadMore} disabled={loading}>
              {loading ? "Cargando…" : "Cargar más"}
            </button>
          </div>
        )}
      </div>

      <style>{`
        @keyframes pulse { 0%{opacity:.6} 50%{opacity:1} 100%{opacity:.6} }
      `}</style>

      <ProviderModal open={open} provider={selected} onClose={handleClose} onUpdated={onProviderUpdated}/>
    </section>
  );
}

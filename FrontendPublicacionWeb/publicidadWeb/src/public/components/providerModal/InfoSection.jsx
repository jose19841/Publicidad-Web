// modules/providers/components/ProviderModal/InfoSection.jsx
import React from "react";
import StarRating from "../shared/StarRating";

export default function InfoSection({ provider }) {
  const {
    description, address, phone, isActive,
    averageRating = 0, totalRatings = 0
  } = provider;

  return (
    <div style={{ display:"grid", gap:12 }}>
      <section style={{ display:"flex", alignItems:"center", gap:10 }}>
        <StarRating value={averageRating} showValue />
        <span style={{ fontSize:13, color:"var(--ct-muted)" }}>
          {totalRatings > 0 ? `${totalRatings} calificación${totalRatings===1?"":"es"}` : "Sin calificaciones"}
        </span>
      </section>

      {description && (
        <section>
          <h4 style={{ margin:"0 0 6px", color:"var(--ct-text)" }}>Descripción</h4>
          <p style={{ margin:0, color:"var(--ct-muted)", whiteSpace:"pre-wrap" }}>{description}</p>
        </section>
      )}

      <section style={{ display:"grid", gap:6 }}>
        <div><strong style={{ color:"var(--ct-muted)" }}>Teléfono:</strong> <span style={{ color:"var(--ct-muted)" }}>{phone || "—"}</span></div>
        <div><strong style={{ color:"var(--ct-muted)" }}>Dirección:</strong> <span style={{ color:"var(--ct-muted)" }}>{address || "—"}</span></div>
      </section>
    </div>
  );
}

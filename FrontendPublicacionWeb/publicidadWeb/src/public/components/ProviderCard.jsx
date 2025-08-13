// modules/providers/components/ProviderCard.jsx
import React from "react";
import "../styles/theme.css";
import StarRating from "./StarRating";

function truncate(t = "", max = 100) {
  return t.length > max ? t.slice(0, max).trim() + "…" : t;
}
function initials(name = "", last = "") {
  const n = (name || "").trim()[0] || "";
  const l = (last || "").trim()[0] || "";
  return (n + l).toUpperCase() || "CT";
}

export default function ProviderCard({ provider, onView, onContact }) {
  const {
    name, lastName, description, categoryName, photoUrl,
    averageRating = 0, totalRatings = 0
  } = provider || {};
  const fullName = [name, lastName].filter(Boolean).join(" ");

  return (
    <article
      style={{
        background: "var(--ct-surface)",
        border: "1px solid var(--ct-border)",
        borderRadius: 14,
        padding: 12,
        display: "grid",
        gridTemplateColumns: "72px 1fr",
        gap: 12,
      }}
      aria-label={`Prestador ${fullName}, rubro ${categoryName}`}
    >
      {/* Foto */}
      <div
        style={{
          width: 72, height: 72, borderRadius: 12, overflow: "hidden",
          background: "#0b1b2b", display: "grid", placeItems: "center"
        }}
      >
        {photoUrl ? (
          <img src={photoUrl} alt={`Foto de ${fullName}`} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
        ) : (
          <span style={{ color: "var(--ct-muted)", fontWeight: 700 }}>{initials(name, lastName)}</span>
        )}
      </div>

      {/* Info */}
      <div style={{ display: "grid", gap: 8 }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: 8 }}>
          <h3 style={{ margin: 0, color: "var(--ct-text)", fontSize: 16 }}>{fullName || "Prestador"}</h3>
          {categoryName && (
            <span style={{
              border: "1px solid var(--ct-border)",
              borderRadius: 999, padding: "4px 8px", fontSize: 12, color: "var(--ct-muted)"
            }}>
              {categoryName}
            </span>
          )}
        </div>

        {/* Rating resumido */}
        <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
          <StarRating value={averageRating} />
          <span style={{ fontSize: 12, color: "var(--ct-muted)" }}>
            {totalRatings > 0 ? `${averageRating.toFixed(1)} ➖ ${totalRatings} calificaciones` : "Sin calificaciones"}
          </span>
        </div>

        {!!description && (
          <p style={{ margin: 0, color: "var(--ct-muted)", fontSize: 14 }}>
            {truncate(description, 110)}
          </p>
        )}

        <div style={{ display: "flex", gap: 8, marginTop: 2 }}>
          <button className="ct-btn" onClick={() => onView?.(provider)}>Ver más</button>
          {onContact && <button className="ct-btn primary" onClick={() => onContact?.(provider)}>Contactar</button>}
        </div>
      </div>
    </article>
  );
}

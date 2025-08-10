// ProviderModal.jsx (sin fechas)
import React, { useEffect } from "react";
import "../styles/theme.css";

export default function ProviderModal({ open, provider, onClose }) {
  useEffect(() => {
    if (!open) return;
    const onKey = (e) => e.key === "Escape" && onClose?.();
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [open, onClose]);

  if (!open || !provider) return null;

  const { name, lastName, address, phone, description, photoUrl, isActive, categoryName } = provider;
  const fullName = [name, lastName].filter(Boolean).join(" ");

  const waHref = phone ? `https://wa.me/${phone.replace(/[^0-9]/g, "")}` : null;
  const telHref = phone ? `tel:${phone}` : null;

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="provider-title"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
      style={{
        position: "fixed", inset: 0, background: "rgba(0,0,0,0.5)",
        display: "grid", placeItems: "center", padding: 16, zIndex: 50
      }}
    >
      <div style={{
        width: "min(780px, 100%)",
        background: "var(--ct-bg)",
        border: "1px solid var(--ct-border)",
        borderRadius: 16,
        overflow: "hidden",
        boxShadow: "0 10px 40px rgba(0,0,0,0.4)"
      }}>
        {/* Header */}
        <div style={{
          display: "flex", alignItems: "center", gap: 12,
          background: "var(--ct-surface)",
          borderBottom: "1px solid var(--ct-border)", padding: 14
        }}>
          <div style={{ width: 56, height: 56, borderRadius: 12, overflow: "hidden", background: "#0b1b2b", display: "grid", placeItems: "center" }}>
            {photoUrl ? (
              <img src={photoUrl} alt="" style={{ width: "100%", height: "100%", objectFit: "cover" }} />
            ) : (
              <span style={{ color: "var(--ct-muted)", fontWeight: 700 }}>CT</span>
            )}
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <h3 id="provider-title" style={{ margin: 0, color: "var(--ct-text)" }}>{fullName || "Prestador"}</h3>
            <p style={{ margin: 0, color: "var(--ct-muted)" }}>{categoryName ?? "—"}</p>
          </div>
          <button className="ct-btn" onClick={onClose} aria-label="Cerrar">Cerrar</button>
        </div>

        {/* Body */}
        <div style={{ padding: 16, display: "grid", gap: 12 }}>
          {description && (
            <section>
              <h4 style={{ margin: "0 0 6px", color: "var(--ct-text)" }}>Descripción</h4>
              <p style={{ margin: 0, color: "var(--ct-muted)", whiteSpace: "pre-wrap" }}>{description}</p>
            </section>
          )}

          <section style={{ display: "grid", gap: 8 }}>
            <h4 style={{ margin: "8px 0 0", color: "var(--ct-text)" }}>Información</h4>
            <div style={{ display: "grid", gap: 6 }}>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Teléfono:</strong> {phone || "—"}
              </div>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Dirección:</strong> {address || "—"}
              </div>
              <div style={{ color: "var(--ct-muted)" }}>
                <strong style={{ color: "var(--ct-text)" }}>Estado:</strong> {isActive ? "Activo" : "No disponible"}
              </div>
            </div>
          </section>
        </div>

        {/* Footer */}
        <div style={{
          padding: 14, borderTop: "1px solid var(--ct-border)",
          background: "var(--ct-surface)", display: "flex", gap: 10, justifyContent: "flex-end"
        }}>
          {waHref && (
            <a className="ct-btn primary" href={waHref} target="_blank" rel="noreferrer">Contactar por WhatsApp</a>
          )}
          {telHref && (
            <a className="ct-btn" href={telHref}>Llamar</a>
          )}
          <button className="ct-btn" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  );
}

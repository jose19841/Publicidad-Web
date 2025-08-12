// modules/ratings/components/RateModal.jsx
import React, { useState } from "react";
import StarRating from "./StarRating";
import "../styles/theme.css";

export default function RateModal({ open, onClose, onConfirm }) {
  const [score, setScore] = useState(5);

  if (!open) return null;
  return (
    <div
      role="dialog"
      aria-modal="true"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,.5)",
        display: "grid",
        placeItems: "center",
        padding: 16,
        zIndex: 60,
      }}
    >
      <div
        style={{
          width: 380,
          background: "var(--ct-bg)",
          border: "1px solid var(--ct-border)",
          borderRadius: 12,
          overflow: "hidden",
        }}
      >
        <header
          style={{
            padding: 12,
            borderBottom: "1px solid var(--ct-border)",
            background: "var(--ct-surface)",
          }}
        >
          <strong style={{ color: "var(--ct-focus)" }}>Calificar</strong>
        </header>
        <div style={{ padding: 16, display: "grid", gap: 12 }}>
          <div className="d-flex justify-content-center">
            <StarRating value={score} showValue />
          </div>
          <div className="d-flex justify-content-center">
            <input
              type="range"
              min="1"
              max="5"
              value={score}
              onChange={(e) => setScore(+e.target.value)}
              className="ct-input"
            />
          </div>
        </div>
        <footer
          style={{
            padding: 12,
            borderTop: "1px solid var(--ct-border)",
            background: "var(--ct-surface)",
            display: "flex",
            gap: 8,
            justifyContent: "flex-end",
          }}
        >
          <button className="ct-btn" onClick={onClose}>
            Cancelar
          </button>
          <button className="ct-btn primary" onClick={() => onConfirm?.(score)}>
            Confirmar
          </button>
        </footer>
      </div>
    </div>
  );
}

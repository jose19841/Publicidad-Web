import React, { useState } from "react";
import StarRating from "../shared/StarRating";
import "../../styles/theme.css";
import "../../styles/RateModal.css";

export default function RateModal({ open, onClose, onConfirm }) {
  const [score, setScore] = useState(5);

  if (!open) return null;

  return (
    <div
      role="dialog"
      aria-modal="true"
      className="rate-modal-backdrop"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
    >
      <div className="rate-modal">
        <header className="rate-modal-header">
          <strong>Calificar</strong>
        </header>

        <div className="rate-modal-body">
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

        <footer className="rate-modal-footer">
          <button className="ct-btn" onClick={onClose}>
            Cancelar
          </button>
          <button
            className="ct-btn primary"
            onClick={() => onConfirm?.(score)}
          >
            Confirmar
          </button>
        </footer>
      </div>
    </div>
  );
}

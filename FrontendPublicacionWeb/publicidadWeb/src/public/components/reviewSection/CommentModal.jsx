import React, { useState } from "react";
import "../../styles/CommentModal.css";

export default function CommentModal({ open, onClose, onConfirm }) {
  const [content, setContent] = useState("");

  if (!open) return null;

  return (
    <div
      role="dialog"
      aria-modal="true"
      className="comment-modal-overlay"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
    >
      <div className="comment-modal-container">
        <header className="comment-modal-header">
          <strong className="comment-modal-title">Dejar comentario</strong>
        </header>

        <div className="comment-modal-body">
          <textarea
            className="ct-input w-100"
            placeholder="Escribí tu experiencia…"
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
        </div>

        <footer className="comment-modal-footer">
          <button className="ct-btn" onClick={onClose}>
            Cancelar
          </button>
          <button
            className="ct-btn primary"
            onClick={() => {
              onConfirm?.(content);
              setContent(""); // limpiar textarea
              onClose?.(); // cerrar modal
            }}
            disabled={!content.trim()}
          >
            Publicar
          </button>
        </footer>
      </div>
    </div>
  );
}

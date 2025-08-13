// modules/comments/components/CommentModal.jsx
import React, { useState } from "react";

export default function CommentModal({ open, onClose, onConfirm }) {
  const [content, setContent] = useState("");

  if (!open) return null;
  return (
    <div role="dialog" aria-modal="true" onClick={(e)=>e.target===e.currentTarget && onClose?.()}
         style={{ position:"fixed", inset:0, background:"rgba(0,0,0,.5)", display:"grid", placeItems:"center", padding:16, zIndex:60 }}>
      <div style={{ width:520, background:"var(--ct-bg)", border:"1px solid var(--ct-border)", borderRadius:12, overflow:"hidden" }}>
        <header style={{ padding:12, borderBottom:"1px solid var(--ct-border)", background:"var(--ct-surface)" }}>
          <strong style={{color:"var(--ct-focus)"}}>Dejar comentario</strong>
        </header>
        <div style={{ padding:16, display:"grid", gap:12 }}>
          <textarea className="ct-input" placeholder="Escribí tu experiencia…" value={content} onChange={e=>setContent(e.target.value)} />
        </div>
        <footer style={{ padding:12, borderTop:"1px solid var(--ct-border)", background:"var(--ct-surface)", display:"flex", gap:8, justifyContent:"flex-end" }}>
          <button className="ct-btn" onClick={onClose}>Cancelar</button>
          <button className="ct-btn primary" onClick={()=>onConfirm?.(content)} disabled={!content.trim()}>Publicar</button>
        </footer>
      </div>
    </div>
  );
}

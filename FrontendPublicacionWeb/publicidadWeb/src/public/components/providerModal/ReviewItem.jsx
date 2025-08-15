// ProviderModal/ReviewItem.jsx
import React from "react";

import StarRating from "../StarRating";
export default function ReviewItem({ comment }) {
  return (
    <article style={{ border:"1px solid var(--ct-border)", borderRadius:12, padding:10, background:"var(--ct-surface)" }}>
      <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between" }}>
        <div style={{ display:"flex", alignItems:"center", gap:8 }}>
          <strong style={{ color:"var(--ct-text)" }}>{comment.username || "Usuario"}</strong>
          <span style={{ fontSize:12, color:"var(--ct-muted)" }}>{comment.createdAt ? new Date(comment.createdAt).toLocaleDateString() : ""}</span>
        </div>
        
      </div>
      <p style={{ margin:"6px 0 0", color:"var(--ct-muted)" }}>{comment.content}</p>
    </article>
  );
}

// ProviderModal/ReviewList.jsx
import React from "react";
import ReviewItem from "./ReviewItem";
export default function ReviewList({ comments=[] }) {
  if (!comments.length) {
    return <div style={{ textAlign:"center", color:"var(--ct-muted)", padding:20, border:"1px dashed var(--ct-border)", borderRadius:12 }}>Aún no hay opiniones.</div>;
  }
  return (
    <div style={{ display:"grid", gap:10, maxHeight:320, overflow:"auto" }}>
      {comments.map(c => <ReviewItem key={c.id} comment={c} />)}
    </div>
  );
}

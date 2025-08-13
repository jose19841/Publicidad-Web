// ProviderModal/ReviewsSection.jsx
import React, { useState } from "react";
import StarRating from "../StarRating";
import ReviewsToolbar from "./ReviewsToolbar";
import ReviewList from "./ReviewList";
import Pagination from "./Pagination";

export default function ReviewsSection({ averageScore, ratingsCount, comments }) {
  const [order, setOrder] = useState("recientes");
  const [page, setPage] = useState(1);

  return (
    <section style={{ display:"grid", gap:12 }}>
      <div style={{ display:"flex", alignItems:"center", gap:10, justifyContent:"space-between" }}>
        <div style={{ display:"flex", alignItems:"center", gap:10 }}>
          <StarRating value={averageScore} showValue />
          <span style={{ fontSize:13, color:"var(--ct-muted)" }}>
            {ratingsCount > 0 ? `${ratingsCount} calificación${ratingsCount===1?"":"es"}` : "—"}
          </span>
        </div>
        <ReviewsToolbar value={order} onChange={setOrder} />
      </div>

      <ReviewList comments={comments} />

      <Pagination page={page} onPrev={() => setPage(p => Math.max(1, p-1))} onNext={() => setPage(p => p+1)} />
    </section>
  );
}

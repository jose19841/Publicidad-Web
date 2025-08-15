// ProviderModal/ReviewsSection.jsx
import React, { useMemo, useState, useEffect } from "react";
import StarRating from "../StarRating";
import ReviewsToolbar from "./ReviewsToolbar";
import ReviewList from "./ReviewList";
import Pagination from "./Pagination";

export default function ReviewsSection({ averageScore, ratingsCount, comments = [] }) {
  const [order, setOrder] = useState("recientes"); // "recientes" | "mejores" | "peores" (ejemplos)
  const [page, setPage] = useState(1);
  const pageSize = 3;

  // 1) Resetear a la página 1 si cambia el orden
  useEffect(() => { setPage(1); }, [order]);

// 2) Ordenar según 'order'
const sorted = useMemo(() => {
  const arr = [...comments];
  switch (order) {
    case "antiguas":
      // más antiguas primero
      return arr.sort((a, b) => new Date(a.createdAt ?? 0) - new Date(b.createdAt ?? 0));
    case "recientes":
    default:
      // más recientes primero
      return arr.sort((a, b) => new Date(b.createdAt ?? 0) - new Date(a.createdAt ?? 0));
  }
}, [comments, order]);

  // 3) Paginación
  const totalPages = Math.max(1, Math.ceil(sorted.length / pageSize));
  const currentPage = Math.min(page, totalPages); // por si cambian comentarios y quedaste “fuera de rango”
  const start = (currentPage - 1) * pageSize;
  const paginated = sorted.slice(start, start + pageSize);

  // 4) Handlers Prev / Next con límites
  const handlePrev = () => setPage(p => Math.max(1, p - 1));
  const handleNext = () => setPage(p => Math.min(totalPages, p + 1));

  return (
    <section style={{ display:"grid", gap:12 }}>
      <div style={{ display:"flex", alignItems:"center", gap:10, justifyContent:"space-between" }}>
        <div style={{ display:"flex", alignItems:"center", gap:10 }}>
          <StarRating value={averageScore} showValue />
          <span style={{ fontSize:13, color:"var(--ct-muted)" }}>
            {ratingsCount > 0 ? `➖${ratingsCount} calificacion${ratingsCount===1?"":"es"}` : "—"}
          </span>
          <span style={{ fontSize:13, color:"var(--ct-muted)" }}>
            {comments.length > 0 ? `➖${comments.length} comentario${comments.length===1?"":"s"}` : "—"}
          </span>
        </div>
        <ReviewsToolbar value={order} onChange={setOrder} />
      </div>

      {/* Acá van SOLO los de la página actual */}
      <ReviewList comments={paginated} />

      {/* Pasale totalPages para bloquear botones en extremos */}
      <Pagination
        page={currentPage}
        totalPages={totalPages}
        onPrev={handlePrev}
        onNext={handleNext}
      />
    </section>
  );
}

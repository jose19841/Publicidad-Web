// modules/providers/components/ProviderModal/ReviewsSection.jsx
import React, { useState } from "react";
import StarRating from "../../shared/StarRating";
import ReviewsToolbar from "./ReviewsToolbar";
import ReviewList from "./ReviewList";
import Pagination from "./Pagination";
import useRequireAuth from "../../modules/users/hooks/useRequireAuth";
import useRateProvider from "../../../ratings/hooks/useRateProvider";
import useCreateComment from "../../../comments/hooks/useCreateComment";
import RateModal from "../../../ratings/components/RateModal";
import CommentModal from "../../../comments/components/CommentModal";

export default function ReviewsSection({ provider, onUpdated }) {
  const { id, averageRating = 0, totalRatings = 0, comments = [] } = provider;
  const [order, setOrder] = useState("recientes");
  const [page, setPage] = useState(1);
  const [rateOpen, setRateOpen] = useState(false);
  const [commentOpen, setCommentOpen] = useState(false);

  const { withAuth } = useRequireAuth();
  const { rate, loading: rateLoading } = useRateProvider();
  const { send, loading: commentLoading } = useCreateComment();

  const openRate = () => withAuth(() => setRateOpen(true));
  const openComment = () => withAuth(() => setCommentOpen(true));

  const handleRate = async (score) => {
    await rate({ providerId: id, score });
    setRateOpen(false);
    onUpdated?.(); // refrescá provider (vuelve a llamar al backend)
  };

  const handleComment = async (content) => {
    await send({ providerId: id, content });
    setCommentOpen(false);
    onUpdated?.();
  };

  return (
    <section style={{ display:"grid", gap:12 }}>
      {/* Cabecera */}
      <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", gap:12, flexWrap:"wrap" }}>
        <div style={{ display:"flex", alignItems:"center", gap:10 }}>
          <StarRating value={averageRating} showValue />
          <span style={{ fontSize:13, color:"var(--ct-muted)" }}>
            {totalRatings > 0 ? `${totalRatings} calificación${totalRatings===1?"":"es"}` : "—"}
          </span>
        </div>

        <div style={{ display:"flex", gap:8 }}>
          <button className="ct-btn" onClick={openRate} disabled={rateLoading}>Calificar</button>
          <button className="ct-btn" onClick={openComment} disabled={commentLoading}>Dejar comentario</button>
          <ReviewsToolbar value={order} onChange={setOrder} />
        </div>
      </div>

      <ReviewList comments={comments} />
      <Pagination page={page} onPrev={() => setPage(p=>Math.max(1,p-1))} onNext={() => setPage(p=>p+1)} />

      <RateModal open={rateOpen} onClose={()=>setRateOpen(false)} onConfirm={handleRate} />
      <CommentModal open={commentOpen} onClose={()=>setCommentOpen(false)} onConfirm={handleComment} />
    </section>
  );
}

import StarRating from "../shared/StarRating";
import ReviewsToolbar from "./ReviewsToolbar";

export default function ReviewsHeader({
  averageRating,
  totalRatings,
  order,
  setOrder
}) {
  return (
    <div className="reviews-header">
      <div className="reviews-header-left">
        <StarRating value={averageRating} showValue />
        <span className="reviews-total">
          {totalRatings > 0
            ? `${totalRatings} calificacion${totalRatings === 1 ? "" : "es"}`
            : "—"}
        </span>
      </div>

      <div className="reviews-header-right">
        <ReviewsToolbar value={order} onChange={setOrder} />
      </div>
    </div>
  );
}

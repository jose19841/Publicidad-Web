// ProviderModal/ReviewsSection.jsx
import StarRating from "../StarRating";
import ReviewsToolbar from "./ReviewsToolbar";
import ReviewList from "./ReviewList";
import Pagination from "./Pagination";
import useReviewsSection from "../../hooks/useReviewsSection";

export default function ReviewsSection({ provider, onUpdated, comments = [] }) {
  const { id, averageRating = 0, totalRatings = 0 } = provider;

  const {
    order,
    setOrder,
    page,
    rateOpen,
    setRateOpen,
    commentOpen,
    setCommentOpen,
    rateLoading,
    commentLoading,
    openRate,
    openComment,
    handleRate,
    handleComment,
    goPrevPage,
    goNextPage,
  } = useReviewsSection(id, onUpdated);

  const pageSize = 5; // o el número que uses
  const paginatedComments = comments.slice((page - 1) * pageSize, page * pageSize);

  return (
    <section className="reviews-section">
      <ReviewsHeader
        averageRating={averageRating}
        totalRatings={totalRatings}
        order={order}
        setOrder={setOrder}
        openRate={openRate}
        openComment={openComment}
        rateLoading={rateLoading}
        commentLoading={commentLoading}
      />

      <ReviewsListContainer
        comments={paginatedComments}
        page={page}
        goPrevPage={goPrevPage}
        goNextPage={goNextPage}
      />

      <ReviewsModals
        rateOpen={rateOpen}
        setRateOpen={setRateOpen}
        commentOpen={commentOpen}
        setCommentOpen={setCommentOpen}
        handleRate={handleRate}
        handleComment={handleComment}
      />
    </section>
  );
}

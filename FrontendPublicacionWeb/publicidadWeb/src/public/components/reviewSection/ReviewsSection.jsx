import "../../styles/ReviewsSection.css";
import useReviewsSection from "../../hooks/useReviewsSection";
import ReviewsHeader from "./ReviewsHeader";
import ReviewsListContainer from "./ReviewsListContainer";
import ReviewsModals from "./ReviewsModals";

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
    paginatedComments,
    totalPages
  } = useReviewsSection(id, onUpdated, comments);

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
        totalPages={totalPages}
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

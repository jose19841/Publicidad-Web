import RateModal from "./RateModal";
import CommentModal from "./CommentModal";

export default function ReviewsModals({
  rateOpen,
  setRateOpen,
  commentOpen,
  setCommentOpen,
  handleRate,
  handleComment
}) {
  return (
    <>
      <RateModal
        open={rateOpen}
        onClose={() => setRateOpen(false)}
        onConfirm={handleRate}
      />
      <CommentModal
        open={commentOpen}
        onClose={() => setCommentOpen(false)}
        onConfirm={handleComment}
      />
    </>
  );
}

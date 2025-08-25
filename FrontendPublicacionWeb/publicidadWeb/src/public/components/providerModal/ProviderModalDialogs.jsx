import RateModal from "../reviewSection/RateModal";
import CommentModal from "../reviewSection/CommentModal";

export default function ProviderModalDialogs({ modal }) {
  const {
    rateOpen,
    setRateOpen,
    commentOpen,
    setCommentOpen,
    handleConfirmRate,
    handleConfirmComment,
  } = modal;

  return (
    <>
      <RateModal
        open={rateOpen}
        onClose={() => setRateOpen(false)}
        onConfirm={handleConfirmRate}
      />
      <CommentModal
        open={commentOpen}
        onClose={() => setCommentOpen(false)}
        onConfirm={handleConfirmComment}
      />
    </>
  );
}

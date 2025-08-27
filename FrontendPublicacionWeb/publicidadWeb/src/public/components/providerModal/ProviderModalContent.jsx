import InfoSection from "./InfoSection";
import ReviewsSection from "../reviewSection/ReviewsSection";

export default function ProviderModalContent({ modal, onUpdated }) {
  const { tab, current, comments, commentsLoading } = modal;
  return (
    <div className="provider-modal-body">
      {tab === "info" ? (
        <InfoSection provider={current} />
      ) : (
        <ReviewsSection
          averageScore={current.averageRating ?? 0}
          ratingsCount={current.totalRatings ?? 0}
          comments={comments}
          loading={commentsLoading}
          provider={current}
          onUpdated={onUpdated}
        />
      )}
    </div>
  );
}

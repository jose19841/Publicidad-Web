import ReviewList from "./ReviewList";
import Pagination from "../../components/providerModal/Pagination";

export default function ReviewsListContainer({ comments, page, goPrevPage, goNextPage, totalPages }) {
  return (
    <>
      <ReviewList comments={comments} />
      <Pagination page={page} onPrev={goPrevPage} onNext={goNextPage} totalPages={totalPages} />
    </>
  );
}

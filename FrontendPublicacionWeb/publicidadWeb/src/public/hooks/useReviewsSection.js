// src/modules/providers/hooks/useReviewsSection.js
import useRequireAuth from "../../modules/users/hooks/useRequireAuth";
import useRateProvider from "../hooks/useRateProvider";
import useCreateComment from "../hooks/useCreateComment";
import { useState, useCallback, useMemo } from "react";

export default function useReviewsSection(providerId, onUpdated, comments = []) {
  const [order, setOrder] = useState("recientes");
  const [page, setPage] = useState(1);
  const pageSize = 3;

  const { withAuth } = useRequireAuth();
  const { rate, loading: rateLoading } = useRateProvider();
  const { send, loading: commentLoading } = useCreateComment();

  const openRate = useCallback(() => {
    withAuth(() => setRateOpen(true));
  }, [withAuth]);

  const openComment = useCallback(() => {
    withAuth(() => setCommentOpen(true));
  }, [withAuth]);

  const [rateOpen, setRateOpen] = useState(false);
  const [commentOpen, setCommentOpen] = useState(false);

  const handleRate = useCallback(
    async (score) => {
      await rate({ providerId, score });
      setRateOpen(false);
      onUpdated?.();
    },
    [rate, providerId, onUpdated]
  );

  const handleComment = useCallback(
    async (content) => {
      await send({ providerId, content });
      setCommentOpen(false);
      onUpdated?.();
    },
    [send, providerId, onUpdated]
  );

  const goPrevPage = useCallback(() => {
    setPage((p) => Math.max(1, p - 1));
  }, []);

  const goNextPage = useCallback(() => {
    setPage((p) => p + 1);
  }, []);

  const paginatedComments = useMemo(() => {
    const sorted = [...comments].sort((a, b) => {
      const dateA = new Date(a.createdAt);
      const dateB = new Date(b.createdAt);
      return order === "recientes" ? dateB - dateA : dateA - dateB;
    });
    return sorted.slice((page - 1) * pageSize, page * pageSize);
  }, [comments, order, page]);

  const totalPages = Math.ceil(comments.length / pageSize);

  return {
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
  };
}


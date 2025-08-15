// src/public/components/providerModal/ProviderModal.jsx
import { useCallback, useEffect, useState } from "react";
import Swal from "sweetalert2";
import { useAuth } from "../../../context/AuthContext";
import useRequireAuth from "../../../modules/users/hooks/useRequireAuth";
import CommentModal from "../../components/CommentModal";
import RateModal from "../../components/RateModal";
import useComments from "../../hooks/useComments";
import useCreateComment from "../../hooks/useCreateComment";
import useRateProvider from "../../hooks/useRateProvider";
import { getProviderById } from "../../services/providerService";
import Header from "./Header";
import InfoSection from "./InfoSection";
import ReviewsSection from "./ReviewsSection";
import Tabs from "./Tabs";

export default function ProviderModal({ open, provider, onClose, onUpdated }) {
  const [tab, setTab] = useState("info");
  const [rateOpen, setRateOpen] = useState(false);
  const [commentOpen, setCommentOpen] = useState(false);
  const { setLoginOpen } = useAuth();

  const [current, setCurrent] = useState(provider);

  useEffect(() => {
    setCurrent(provider);
  }, [provider]);

  const { withAuth } = useRequireAuth();
  const { rate, loading: rateLoading } = useRateProvider();
  const { send, loading: commentLoading } = useCreateComment();

  // ✅ hook para traer comentarios
  const {
    comments,
    loading: commentsLoading,
    refresh: refreshComments,
  } = useComments(current?.id);

  const ok = (title, text) => {
    if (Swal?.fire) {
      return Swal.fire({
        icon: "success",
        title,
        text,
        timer: 1600,
        showConfirmButton: false,
      });
    }
    alert(`${title}\n${text || ""}`);
  };

  const fail = (title, e) => {
    const msg = e?.response?.data?.error || e?.error || "Ocurrió un error";
    if (Swal?.fire) return Swal.fire({ icon: "error", title, text: msg });
    alert(`${title}\n${msg}`);
  };

  const refreshProvider = useCallback(async () => {
    if (!current?.id) return;
    const fresh = await getProviderById(current.id);
    setCurrent(fresh);
  }, [current?.id]);

  useEffect(() => {
    if (!open) return;
    const onKey = (e) => e.key === "Escape" && onClose?.();
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [open, onClose]);

  if (!open || !current) return null;

  const { id, phone } = current;
  const waHref = phone ? `https://wa.me/${phone.replace(/[^0-9]/g, "")}` : null;
  const telHref = phone ? `tel:${phone}` : null;

  const openRate = withAuth(() => setRateOpen(true));
  const openComment = withAuth(() => setCommentOpen(true));

  const handleConfirmRate = async (score) => {
    try {
      await rate({ providerId: id, score: Number(score) });
      refreshProvider();
      onUpdated?.(id);
      setRateOpen(false);
      ok("¡Gracias!", "Tu calificación fue registrada.");
    } catch (e) {
      fail("No se pudo calificar", e);
    }
  };

  const handleConfirmComment = async (content) => {
    try {
      await send({ providerId: id, content });
      refreshComments();
      onUpdated?.(id);
      setCommentOpen(false);
      ok("Comentario enviado", "Gracias por compartir tu experiencia.");
    } catch (e) {
      fail("No se pudo enviar el comentario", e);
    }
  };

  return (
    <div
      role="dialog"
      aria-modal="true"
      onClick={(e) => e.target === e.currentTarget && onClose?.()}
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.5)",
        display: "grid",
        placeItems: "center",
        padding: 16,
        zIndex: 50,
      }}
    >
      <div
        style={{
          width: "min(780px, 100%)",
          background: "var(--ct-bg)",
          border: "1px solid var(--ct-border)",
          borderRadius: 16,
          overflow: "hidden",
        }}
      >
        <Header provider={current} onClose={onClose} />
        <Tabs
          value={tab}
          onChange={setTab}
          count={(current?.totalRatings ?? 0) + (comments?.length ?? 0)}
        />
        <div style={{ padding: 16 }}>
          {tab === "info" ? (
            <InfoSection provider={current} />
          ) : (
            <ReviewsSection
              averageScore={current.averageRating ?? 0}
              ratingsCount={current.totalRatings ?? 0}
              comments={comments}
              loading={commentsLoading}
            />
          )}
        </div>

        <div className="actions-bar">
          <div className="actions-grid">
            <button
              className="ct-btn btn-same"
              onClick={openRate}
              disabled={rateLoading}
            >
              Calificar
            </button>
            <button
              className="ct-btn btn-same"
              onClick={openComment}
              disabled={commentLoading}
            >
              Dejar comentario
            </button>
            {waHref && (
              <a
                className="ct-btn primary btn-same"
                href={waHref}
                target="_blank"
                rel="noreferrer"
              >
                WhatsApp
              </a>
            )}
            {telHref && (
              <a className="ct-btn btn-same" href={telHref}>
                Llamar
              </a>
            )}
            <button className="ct-btn btn-same" onClick={onClose}>
              Cerrar
            </button>
          </div>
        </div>
      </div>

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
    </div>
  );
}

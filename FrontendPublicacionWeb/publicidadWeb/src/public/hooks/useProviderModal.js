// src/public/hooks/useProviderModal.js
import { useState, useEffect, useCallback } from "react";
import Swal from "sweetalert2";
import { useAuth } from "../../context/AuthContext";
import useRequireAuth from "../../modules/users/hooks/useRequireAuth";
import useComments from "../hooks/useComments";
import useCreateComment from "../hooks/useCreateComment";
import useRateProvider from "../hooks/useRateProvider";
import { getProviderById } from "../services/providerService";

export default function useProviderModal(provider, open, onClose, onUpdated) {
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

  const openRate = withAuth(() => setRateOpen(true));
  const openComment = withAuth(() => setCommentOpen(true));

  const handleConfirmRate = async (score) => {
    try {
      await rate({ providerId: current.id, score: Number(score) });
      refreshProvider();
      onUpdated?.(current.id);
      setRateOpen(false);
      ok("¡Gracias!", "Tu calificación fue registrada.");
    } catch (e) {
      fail("No se pudo calificar", e);
    }
  };

  const handleConfirmComment = async (content) => {
    try {
      await send({ providerId: current.id, content });
      refreshComments();
      onUpdated?.(current.id);
      setCommentOpen(false);
      ok("Comentario enviado", "Gracias por compartir tu experiencia.");
    } catch (e) {
      fail("No se pudo enviar el comentario", e);
    }
  };

  // Helpers para formatear números
  const formatPhoneForWhatsApp = (phone) => {
    if (!phone) return null;
    let clean = phone.replace(/\D/g, ""); // solo dígitos

    // Si no tiene prefijo de país, le agregamos Argentina por defecto
    if (!clean.startsWith("54")) {
      clean = "54" + clean;
    }

    return `https://wa.me/${clean}`;
  };

  const formatPhoneForTel = (phone) => {
    if (!phone) return null;
    let clean = phone.replace(/\D/g, "");
    if (!clean.startsWith("+")) {
      clean = "+54" + clean;
    }
    return `tel:${clean}`;
  };

  const waHref = formatPhoneForWhatsApp(current?.phone);
  const telHref = formatPhoneForTel(current?.phone);

  return {
    tab, setTab,
    rateOpen, setRateOpen,
    commentOpen, setCommentOpen,
    current,
    rateLoading,
    commentLoading,
    comments,
    commentsLoading,
    waHref,
    telHref,
    openRate,
    openComment,
    handleConfirmRate,
    handleConfirmComment,
  };
}

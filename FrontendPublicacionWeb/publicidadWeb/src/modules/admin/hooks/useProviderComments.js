// src/hooks/useProviderComments.js
import { useState } from "react";
import Swal from "sweetalert2";
import { getCommentsByProvider, deleteComment } from "../services/commentsService";

export default function useProviderComments() {
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchComments = async (providerId) => {
    try {
      setLoading(true);
      const data = await getCommentsByProvider(providerId);
      setComments(data);
    } catch (err) {
      console.error("Error al cargar comentarios:", err);
      setError("Error al cargar comentarios.");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (commentId) => {
    const result = await Swal.fire({
      title: "¿Eliminar comentario?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    });

    if (result.isConfirmed) {
      try {
        await deleteComment(commentId);
        setComments((prev) => prev.filter((c) => c.id !== commentId));
        Swal.fire("Eliminado", "El comentario ha sido eliminado", "success");
      } catch (err) {
        console.error("Error al eliminar comentario:", err);
        Swal.fire("Error", "No se pudo eliminar el comentario", "error");
      }
    }
  };

  return { comments, loading, error, fetchComments, handleDelete };
}

// src/components/CommentsModal.jsx
import React, { useEffect, useState } from "react";
import useProviderComments from "../hooks/useProviderComments";

const CommentsModal = ({ providerId, show, onClose }) => {
  const { comments, loading, error, fetchComments, handleDelete } = useProviderComments();
  const [page, setPage] = useState(1);
  const pageSize = 5;

  useEffect(() => {
    if (show && providerId) {
      fetchComments(providerId);
      setPage(1); // reset al abrir
    }
  }, [show, providerId]);

  if (!show) return null;

  // cálculos para paginación
  const totalPages = Math.ceil(comments.length / pageSize);
  const startIndex = (page - 1) * pageSize;
  const visibleComments = comments.slice(startIndex, startIndex + pageSize);

  return (
    <div className="modal show fade d-block" tabIndex="-1">
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              Comentarios del proveedor #{providerId}{" "}
              <small className="text-muted">({comments.length})</small>
            </h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {loading && <p>Cargando comentarios...</p>}
            {error && <p className="text-danger">{error}</p>}
            {!loading && comments.length === 0 && <p>No hay comentarios disponibles.</p>}

            <ul className="list-group mb-3">
              {visibleComments.map((c) => (
                <li key={c.id} className="list-group-item">
                  <div className="d-flex justify-content-between align-items-start">
                    <div>
                      <strong>{c.username}</strong> <br />
                      <small className="text-muted">
                        {new Date(c.createdAt).toLocaleString()}
                      </small>
                      <p className="mb-1">{c.content}</p>
                    </div>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDelete(c.id)}
                    >
                      Eliminar
                    </button>
                  </div>
                </li>
              ))}
            </ul>

            {totalPages > 1 && (
              <nav>
                <ul className="pagination justify-content-center mb-0">
                  <li className={`page-item${page === 1 ? " disabled" : ""}`}>
                    <button
                      className="page-link"
                      onClick={() => setPage((prev) => prev - 1)}
                    >
                      Anterior
                    </button>
                  </li>
                  {Array.from({ length: totalPages }, (_, idx) => (
                    <li
                      key={idx + 1}
                      className={`page-item${page === idx + 1 ? " active" : ""}`}
                    >
                      <button
                        className="page-link"
                        onClick={() => setPage(idx + 1)}
                      >
                        {idx + 1}
                      </button>
                    </li>
                  ))}
                  <li className={`page-item${page === totalPages ? " disabled" : ""}`}>
                    <button
                      className="page-link"
                      onClick={() => setPage((prev) => prev + 1)}
                    >
                      Siguiente
                    </button>
                  </li>
                </ul>
              </nav>
            )}
          </div>
          <div className="modal-footer">
            <button className="btn btn-secondary" onClick={onClose}>
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CommentsModal;

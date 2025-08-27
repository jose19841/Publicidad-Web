// src/components/providers-section/LoadMore.jsx
export default function LoadMore({ page, totalPages, loading, onClick }) {
  if (page >= totalPages - 1) return null; // estamos en la última página
  return (
    <div className="load-more-container">
      <button className="ct-btn" onClick={onClick} disabled={loading}>
        {loading ? "Cargando…" : "Cargar más"}
      </button>
    </div>
  );
}

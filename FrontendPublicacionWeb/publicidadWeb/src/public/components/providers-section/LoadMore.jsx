// src/components/providers-section/LoadMore.jsx
export default function LoadMore({ show, loading, onClick }) {
  if (!show) return null;
  return (
    <div className="load-more-container">
      <button className="ct-btn" onClick={onClick} disabled={loading}>
        {loading ? "Cargando…" : "Cargar más"}
      </button>
    </div>
  );
}

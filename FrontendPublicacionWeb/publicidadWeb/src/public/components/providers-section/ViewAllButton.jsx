// src/components/providers-section/ViewAllButton.jsx
export default function ViewAllButton({ onClick }) {
  return (
    <div className="view-all-container">
      <button
        onClick={onClick}
        className="btn btn-primary providers-viewall-btn"
      >
        Ver todos los proveedores
      </button>
    </div>
  );
}

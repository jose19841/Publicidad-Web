// src/components/providers-section/ProvidersGrid.jsx
import ProviderCard from "./ProviderCard";
import ProviderCardModern from "./ProviderCardModern";

export default function ProvidersGrid({
  items,
  paginatedItems,
  loading,
  onView,
  onContact,
}) {
  return (
    <div role="list" className="providers-list">
      {/* Skeletons */}
      {loading && items.length === 0 &&
        Array.from({ length: 6 }).map((_, i) => (
          <div key={i} aria-hidden="true" className="skeleton-card" />
        ))}

      {/* Vacío */}
      {!loading && items.length === 0 && (
        <div role="status" className="empty-state">
          No se encontraron prestadores. Probá con otro término (ej:
          “electricista”, “pintor”).
        </div>
      )}

      {/* Cards */}
      {paginatedItems.map((p) => (
        <div role="listitem" key={p.id}>
          <ProviderCardModern provider={p} onView={onView} onContact={onContact} />
        </div>
      ))}
    </div>
  );
}

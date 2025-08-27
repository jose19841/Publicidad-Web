import "../../styles/theme.css";
import "../../styles/ProvidersSection.css";
import useProvidersSection from "../../hooks/useProvidersSection";
import ProvidersTitle from "./ProvidersTitle";
import ViewAllButton from "./ViewAllButton";
import ProvidersGrid from "./ProvidersGrid";
import LoadMore from "./LoadMore";
import ProviderModal from "../../components/providerModal/ProviderModal";

export default function ProvidersSection({
  title = "Prestadores",
  items = [],             // 👈 si viene búsqueda, se usa esto
  loading,
  onProviderUpdated,
}) {
  const {
    open,
    selected,
    paginatedItems,
    handleLoadMore,
    handleView,
    handleClose,
    handleContact,
    handleViewAll,
    page,
    totalPages,
  } = useProvidersSection(items);

  return (
    <section aria-labelledby="providers-title" className="providers-section">
      <div className="ct-container providers-container">
        <ProvidersTitle title={title} />

        {/* Botón Ver todos → resetea a la página 0 */}
        <ViewAllButton onClick={handleViewAll} />

        {/* Grid de prestadores */}
        <ProvidersGrid
          items={items}                // resultados de búsqueda (si hay)
          paginatedItems={paginatedItems} // datos paginados (si no hay búsqueda)
          loading={loading}
          onView={handleView}
          onContact={handleContact}
        />

        {/* Botón Cargar más */}
        <LoadMore
          show={items.length === 0 && !loading && page < totalPages - 1}
          loading={loading}
          onClick={handleLoadMore}
        />
      </div>

      <ProviderModal
        open={open}
        provider={selected}
        onClose={handleClose}
        onUpdated={onProviderUpdated}
      />
    </section>
  );
}

// src/components/ProvidersSection.jsx
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
  items = [],
  loading,
  onProviderUpdated,
  fetchAllProviders,
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
  } = useProvidersSection(items, fetchAllProviders);

  return (
    <section aria-labelledby="providers-title" className="providers-section">
      <div className="ct-container providers-container">
        <ProvidersTitle title={title} />
        <ViewAllButton onClick={handleViewAll} />

        <ProvidersGrid
          items={items}
          paginatedItems={paginatedItems}
          loading={loading}
          onView={handleView}
          onContact={handleContact}
        />

        <LoadMore
          show={paginatedItems.length < items.length}
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

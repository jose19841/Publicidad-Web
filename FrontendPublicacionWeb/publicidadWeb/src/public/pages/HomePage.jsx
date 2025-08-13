import Header from "../components/Header";
import SearchHero from "../components/SearchHero";
import ProvidersSection from "../components/ProvidersSection";
import useProviders from "../hooks/useProviders";
import HowItWorksSection from "../components/HowItWorksSection";
import ContactSection from "../components/ContactSection";
import Footer from "../components/Footer";
import { getProviderById } from "../services/providerService";
import { useEffect } from "react";

export default function HomePage() {
  const {
    list,
    loading,
    error,
    setQuery,
    updateProviderInList,
    fetchAllProviders,
  } = useProviders();
  const handleSearch = (value) => setQuery(value);

  const handleProviderUpdated = async (id) => {
    const fresh = await getProviderById(id);
    updateProviderInList(fresh);
  };

  useEffect(() => {
    fetchAllProviders(); // Carga todos los proveedores al montar
  }, [fetchAllProviders]);
  return (
    <>
      <Header />
      <SearchHero onSearch={handleSearch} />
      {error && (
        <div
          className="ct-container"
          style={{ color: "tomato", paddingTop: 12 }}
        >
          {error}
        </div>
      )}

      <ProvidersSection
        title="Prestadores"
        items={list}
        loading={loading}
        hasMore={false}
        onLoadMore={() => {}}
        onProviderUpdated={handleProviderUpdated}
        fetchAllProviders={fetchAllProviders} // Pasamos la función para cargar todos los proveedores
      />
      <HowItWorksSection />
      <ContactSection />
      <Footer />
    </>
  );
}

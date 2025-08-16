// src/pages/HomePage.jsx
import Header from "../components/Header";
import SearchHero from "../components/SearchHero";
import ProvidersSection from "../components/providers-section/ProvidersSection";
import HowItWorksSection from "../components/how-it-works/HowItWorksSection";
import ContactSection from "../components/contact/ContactSection";
import Footer from "../components/Footer";
import ErrorMessage from "../components/shared/ErrorMessage";
import useHomePage from "../hooks/useHomePage";

export default function HomePage() {
  const {
    list,
    loading,
    error,
    onSearch,
    onProviderUpdated,
    fetchAllProviders,
  } = useHomePage();

  return (
    <>
      <Header />
      <SearchHero onSearch={onSearch} />
      <ErrorMessage message={error} />
      <ProvidersSection
        title="Prestadores"
        items={list}
        loading={loading}
        hasMore={false}
        onLoadMore={() => {}}
        onProviderUpdated={onProviderUpdated}
        fetchAllProviders={fetchAllProviders}
      />
      <HowItWorksSection />
      <ContactSection />
      <Footer />
    </>
  );
}

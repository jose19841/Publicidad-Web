import Header from "../components/Header";
import SearchHero from "../components/SearchHero";
import ProvidersSection from "../components/ProvidersSection";
import useProviders from "../hooks/useProviders";
import HowItWorksSection from "../components/HowItWorksSection";
import ContactSection from "../components/ContactSection";
import Footer from "../components/Footer";
import { getProviderById } from "../services/providerService"


export default function HomePage(){
  const { list, loading, error, setQuery, updateProviderInList } = useProviders();

  const handleSearch = (value) => setQuery(value); 
   
  const handleProviderUpdated = async (id) => {
    const fresh = await getProviderById(id);
    updateProviderInList(fresh);
  };

  return (
    <>
      <Header />
      <SearchHero onSearch={handleSearch} />
      {error && (
        <div className="ct-container" style={{ color: "tomato", paddingTop: 12 }}>
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
      />
       <HowItWorksSection />  
         <ContactSection />
         <Footer />
    </>
  );
}

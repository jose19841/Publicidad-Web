// src/components/SearchHero.jsx
import "../styles/theme.css";
import "../styles/SearchHero.css";
import useSearchHero from "../hooks/useSearchHero";
import SearchTitle from "./search-hero/SearchTitle";
import SearchForm from "./search-hero/SearchForm";
import SearchTips from "./search-hero/SearchTips";

export default function SearchHero({ onSearch }) {
  const { query, handleChange, handleSubmit } = useSearchHero({ onSearch });

  return (
    <section className="search-hero" aria-label="Portada y buscador">
      <div className="ct-container search-hero-container">
        <SearchTitle />
        <SearchForm
          query={query}
          onChange={handleChange}
          onSubmit={handleSubmit}
        />
        <SearchTips />
      </div>
    </section>
  );
}

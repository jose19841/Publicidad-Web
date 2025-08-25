// src/components/search-hero/SearchForm.jsx
export default function SearchForm({ query, onChange, onSubmit }) {
  return (
    <form
      onSubmit={onSubmit}
      role="search"
      aria-label="Buscar servicios"
      className="search-hero-form"
    >
      <label htmlFor="q" className="ct-hidden">¿Qué servicio necesitás?</label>
      <input
        id="q"
        name="q"
        type="text"
        placeholder="Ej: plomería, peluquería, electricidad…"
        value={query}
        onChange={onChange}
        className="search-hero-input"
        autoComplete="off"
      />
      <button type="submit" className="ct-btn primary search-hero-button">
        Buscar
      </button>
    </form>
  );
}

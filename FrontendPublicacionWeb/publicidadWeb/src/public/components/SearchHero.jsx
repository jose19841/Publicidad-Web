  import { useState } from "react";
import "../styles/theme.css";

  export default function SearchHero({ onSearch }) {
    const [query, setQuery] = useState("");

    const handleSubmit = (e) => {
      e.preventDefault();
      const value = query.trim();
      if (!value) return;
      onSearch?.(value);
    };

    return (
      <section
        style={{
          background: "linear-gradient(180deg, #0b1b2b 0%, #12263a 60%, #0b1b2b 100%)",
          color: "var(--ct-text)",
          padding: "56px 0",
          borderBottom: "1px solid var(--ct-border)",
        }}
        aria-label="Portada y buscador"
      >
        <div className="ct-container" style={{ display: "grid", gap: 18 }}>
          <div style={{ maxWidth: 820 }}>
            <h1 style={{ margin: 0, fontSize: "clamp(28px, 4vw, 40px)", lineHeight: 1.1 }}>
              Encontrá servicios confiables en Trenque Lauquen
            </h1>
            <p style={{ margin: "10px 0 0", color: "var(--ct-muted)", fontSize: "clamp(14px, 2.2vw, 18px)" }}>
              Rápido, verificado y cerca tuyo.
            </p>
          </div>

          <form onSubmit={handleSubmit} role="search" aria-label="Buscar servicios"
            style={{
              display: "grid",
              gridTemplateColumns: "1fr auto",
              gap: 10,
              alignItems: "center",
              background: "var(--ct-surface)",
              border: "1px solid var(--ct-border)",
              borderRadius: 14,
              padding: 10,
              marginTop: 10,
            }}
          >
            <label htmlFor="q" className="ct-hidden">¿Qué servicio necesitás?</label>
            <input
              id="q"
              name="q"
              type="text"
              placeholder="Ej: plomería, peluquería, electricidad…"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              style={{
                background: "transparent",
                border: "none",
                outline: "none",
                color: "var(--ct-text)",
                padding: "12px 12px",
                fontSize: 16,
              }}
              autoComplete="off"
            />
            <button type="submit" className="ct-btn primary" style={{ height: 44 }}>
              Buscar
            </button>
          </form>

          {/* Tips / CTA secundarios */}
          <div style={{ display: "flex", flexWrap: "wrap", gap: 8, marginTop: 6 }}>
            <span style={{
              background: "rgba(159,179,200,0.12)",
              border: "1px solid var(--ct-border)",
              color: "var(--ct-muted)",
              padding: "6px 10px",
              borderRadius: 999,
              fontSize: 13
            }}>
              Ejemplos: “electricista”, “pintor”, “mecánico”
            </span>
            <a href="#como-funciona" style={{ color: "var(--ct-primary)", textDecoration: "none", fontSize: 14 }}>
              Cómo funciona →
            </a>
            <a href="#contacto" style={{ color: "var(--ct-primary)", textDecoration: "none", fontSize: 14 }}>
              Contacto →
            </a>
          </div>
        </div>
      </section>
    );
  }

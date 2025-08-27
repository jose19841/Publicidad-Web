// src/components/categories/CategoriesSection.jsx
import React, { useEffect } from "react";
import "../../styles/CategoriesSection.css";
import useCategories from "../../hooks/useCategories";

export default function CategoriesSection() {
  const { categories, loading, error, fetchCategories } = useCategories();

  // Disparar la carga desde el componente (no desde el hook)
  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  return (
    <section className="ct-categories">
        <div className="ct-categories__inner">

        
      <div className="ct-categories__header">
        <h2 className="ct-categories__title">Categorías Disponibles</h2>
        {loading && <span className="ct-categories__hint">Cargando…</span>}
        {error && <span className="ct-categories__error">{error}</span>}
      </div>

      <div className="ct-categories__grid">
        {categories.map((c) => (
          <div className="ct-category" key={c.id} title={c.name}>
            <span className="ct-category__name">{c.name}</span>
          </div>
        ))}
      </div>
      </div>
    </section>
  );
}

// ProviderModal/ReviewsToolbar.jsx
import React from "react";
import "../../styles/theme.css"; 
export default function ReviewsToolbar({ value, onChange }) {
  return (
    <select className="ct-input" value={value} onChange={(e)=>onChange(e.target.value)} aria-label="Ordenar opiniones">
      <option value="recientes">Más recientes</option>
      <option value="mejores">Mejor puntuadas</option>
      <option value="peores">Peor puntuadas</option>
    </select>
  );
}

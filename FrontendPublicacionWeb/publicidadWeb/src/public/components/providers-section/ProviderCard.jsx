import React from "react";
import "../../styles/theme.css";
import "../../styles/ProviderCard.css";
import StarRating from "../shared/StarRating";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import CategoryIcon from "@mui/icons-material/Category";
import PhoneIcon from "@mui/icons-material/Phone";

function truncate(t = "", max = 100) {
  return t.length > max ? t.slice(0, max).trim() + "…" : t;
}

function initials(name = "", last = "") {
  const n = (name || "").trim()[0] || "";
  const l = (last || "").trim()[0] || "";
  return (n + l).toUpperCase() || "CT";
}

export default function ProviderCard({ provider, onView, onContact }) {
  const {
    name,
    lastName,
    description,
    categoryName,
    address,
    phone,
    photoUrl,
    averageRating = 0,
    totalRatings = 0,
  } = provider || {};

  const fullName = [name, lastName].filter(Boolean).join(" ");

  return (
    <article className="provider-card" aria-label={`Prestador ${fullName}`}>
      {/* Foto */}
      <div className="provider-photo">
        {photoUrl ? (
          <img src={photoUrl} alt={`Foto de ${fullName}`} />
        ) : (
          <span className="provider-initials">
            {initials(name, lastName)}
          </span>
        )}
      </div>

      {/* Info */}
      <div className="provider-info">
        <div className="provider-header">
          <h3>{fullName || "Prestador"}</h3>
          {categoryName && (
            <span className="provider-category">
              <CategoryIcon fontSize="small" /> {categoryName}
            </span>
          )}
        </div>

        <div className="provider-rating">
          <StarRating value={averageRating} />
          <span>
            {totalRatings > 0
              ? `${averageRating.toFixed(1)} · ${totalRatings} calificaciones`
              : "Sin calificaciones"}
          </span>
        </div>

        {address && (
          <div className="provider-detail">
            <LocationOnIcon fontSize="small" /> {address}
          </div>
        )}

        {phone && (
          <div className="provider-detail">
            <PhoneIcon fontSize="small" /> {phone}
          </div>
        )}

        {!!description && (
          <p className="provider-description">
            {truncate(description, 90)}
          </p>
        )}

        <div className="provider-actions">
          <button className="ct-btn" onClick={() => onView?.(provider)}>
            Ver más
          </button>
          {onContact && (
            <button
              className="ct-btn primary"
              onClick={() => onContact?.(provider)}
            >
              Contactar
            </button>
          )}
        </div>
      </div>
    </article>
  );
}

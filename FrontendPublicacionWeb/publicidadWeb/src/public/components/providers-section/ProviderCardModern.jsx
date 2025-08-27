import React from "react";
import "../../styles/theme.css";
import "../../styles/ProviderCardModern.css";
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

export default function ProviderCardModern({ provider, onView, onContact }) {
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
    <article className="pcard" aria-label={`Prestador ${fullName || ""}`}>
      {/* Banda izquierda decorativa + avatar */}
      <aside className="pcard__aside">
       <div className={`pcard__badge ${photoUrl ? "has-image" : ""}`}>
          {photoUrl ? (
            <img src={photoUrl} alt={`Foto de ${fullName}`} />
          ) : (
            <span className="pcard__initials">{initials(name, lastName)}</span>
          )}
        </div>
      </aside>

      {/* Contenido */}
      <section className="pcard__content">
        <header className="pcard__header">
          <h3 className="pcard__name">
            <span className="pcard__name-strong">{name || "Prestador"}</span>{" "}
            {lastName && <span className="pcard__name-light">{lastName}</span>}
          </h3>

          {categoryName && (
            <div className="pcard__role">
              <CategoryIcon fontSize="small" />
              <span>{categoryName}</span>
            </div>
          )}
        </header>

        <div className="pcard__meta">
          <div className="pcard__rating">
            <StarRating value={averageRating} />
            <span className="pcard__rating-text">
              {totalRatings > 0
                ? `${averageRating.toFixed(1)} · ${totalRatings} calificaciones`
                : "Sin calificaciones"}
            </span>
          </div>

          <ul className="pcard__list">
            {phone && (
              <li className="pcard__item">
                <PhoneIcon fontSize="small" />
                <span>{phone}</span>
              </li>
            )}
            {address && (
              <li className="pcard__item">
                <LocationOnIcon fontSize="small" />
                <span>{address}</span>
              </li>
            )}
          </ul>
        </div>

        {!!description && (
          <p className="pcard__desc">{truncate(description, 110)}</p>
        )}

        <footer className="pcard__actions">
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
        </footer>
      </section>
    </article>
  );
}

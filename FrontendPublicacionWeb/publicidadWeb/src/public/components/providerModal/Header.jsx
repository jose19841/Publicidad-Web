import React from "react";
import "../../styles/ProviderModalHeader.css";

export default function Header({ provider, onClose }) {
  const { name, lastName, categoryName, photoUrl } = provider;
  const fullName = [name, lastName].filter(Boolean).join(" ");

  return (
    <div className="provider-modal-header">
      <div className="provider-modal-avatar">
        {photoUrl ? (
          <img src={photoUrl} alt="" className="provider-modal-avatar-img" />
        ) : (
          <span className="provider-modal-avatar-placeholder">CT</span>
        )}
      </div>

      <div className="provider-modal-info">
        <h3 className="provider-modal-name">{fullName || "Prestador"}</h3>
        <p className="provider-modal-category">{categoryName ?? "—"}</p>
      </div>

      <button className="ct-btn" onClick={onClose}>Cerrar</button>
    </div>
  );
}

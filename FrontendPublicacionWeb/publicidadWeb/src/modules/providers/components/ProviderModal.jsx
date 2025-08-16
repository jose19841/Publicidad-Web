import React from "react";
import ImageUpload from "./ImageUpload";
import useProviderModalLogic from "../hooks/useProviderModalLogic";

export default function ProviderModal({ isOpen, provider, onClose, onSuccess }) {
  const {
    formValues,
    imageAction,
    setImageAction,
    imageValue,
    setImageValue,
    saving,
    handleChange,
    handleSave
  } = useProviderModalLogic({ provider, onClose, onSuccess });

  if (!isOpen) return null;

  return (
    <div className="modal show" style={{ display: "block" }}>
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {provider ? "Editar Proveedor" : "Nuevo Proveedor"}
            </h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>

          <div className="modal-body">
            {["name", "lastName", "address", "phone"].map((field) => (
              <div className="mb-3" key={field}>
                <label>{field}</label>
                <input
                  className="form-control"
                  name={field}
                  value={formValues[field]}
                  onChange={handleChange}
                />
              </div>
            ))}

            <div className="mb-3">
              <label>Descripción</label>
              <textarea
                className="form-control"
                name="description"
                value={formValues.description}
                onChange={handleChange}
              ></textarea>
            </div>

            <div className="mb-3">
              <label className="form-label">Imagen del proveedor</label>
              <div className="d-flex gap-3 mb-2">
                <div className="form-check">
                  <input
                    type="radio"
                    id="keep"
                    name="imageAction"
                    value="keep"
                    checked={imageAction === "keep"}
                    onChange={() => setImageAction("keep")}
                    disabled={!provider?.photoUrl}
                    className="form-check-input"
                  />
                  <label htmlFor="keep" className="form-check-label">Mantener</label>
                </div>
                <div className="form-check">
                  <input
                    type="radio"
                    id="replace"
                    name="imageAction"
                    value="replace"
                    checked={imageAction === "replace"}
                    onChange={() => setImageAction("replace")}
                    className="form-check-input"
                  />
                  <label htmlFor="replace" className="form-check-label">Reemplazar</label>
                </div>
                <div className="form-check">
                  <input
                    type="radio"
                    id="remove"
                    name="imageAction"
                    value="remove"
                    checked={imageAction === "remove"}
                    onChange={() => setImageAction("remove")}
                    className="form-check-input"
                  />
                  <label htmlFor="remove" className="form-check-label">Quitar</label>
                </div>
              </div>

              {imageAction === "keep" && provider?.photoUrl && (
                <img
                  src={provider.photoUrl}
                  alt="Actual"
                  style={{ maxWidth: "150px", borderRadius: 8 }}
                />
              )}

              {imageAction === "replace" && (
                <ImageUpload value={imageValue} onChange={setImageValue} />
              )}
            </div>

            <div className="form-check">
              <input
                id="isActive"
                type="checkbox"
                className="form-check-input"
                name="isActive"
                checked={formValues.isActive}
                onChange={handleChange}
              />
              <label htmlFor="isActive" className="form-check-label">Activo</label>
            </div>
          </div>

          <div className="modal-footer">
            <button className="btn btn-secondary" onClick={onClose} disabled={saving}>
              Cancelar
            </button>
            <button className="btn btn-primary" onClick={handleSave} disabled={saving}>
              {saving ? "Guardando..." : "Guardar"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

// src/modules/providers/components/ProviderModal.jsx
import React, { useState, useEffect } from "react";
import Swal from "sweetalert2";

export default function ProviderModal({ isOpen, provider, onClose, table }) {
  const [formValues, setFormValues] = useState({
    name: "",
    lastName: "",
    address: "",
    phone: "",
    description: "",
    photoUrl: "",
    isActive: true,
  });

  // Cargar datos al abrir modal
  useEffect(() => {
    if (provider) {
      setFormValues({
        name: provider.name || "",
        lastName: provider.lastName || "",
        address: provider.address || "",
        phone: provider.phone || "",
        description: provider.description || "",
        photoUrl: provider.photoUrl || "",
        isActive: provider.isActive ?? true,
      });
    }
  }, [provider]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      await table.handleUpdate(provider.id, formValues);
      Swal.fire("Éxito", "Proveedor actualizado correctamente", "success");
      onClose();
    } catch (err) {
      Swal.fire(
        "Error",
        err?.response?.data?.message || "No se pudo actualizar el proveedor",
        "error"
      );
    }
  };

  return (
    <div className="modal show" style={{ display: "block" }}>
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {provider ? "Editar Proveedor" : "Nuevo Proveedor"}
            </h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
            ></button>
          </div>
          <div className="modal-body">
            <div className="mb-3">
              <label>Nombre</label>
              <input
                className="form-control"
                name="name"
                value={formValues.name}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label>Apellido</label>
              <input
                className="form-control"
                name="lastName"
                value={formValues.lastName}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label>Dirección</label>
              <input
                className="form-control"
                name="address"
                value={formValues.address}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label>Teléfono</label>
              <input
                className="form-control"
                name="phone"
                value={formValues.phone}
                onChange={handleChange}
              />
            </div>
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
              <label>URL Foto</label>
              <input
                className="form-control"
                name="photoUrl"
                value={formValues.photoUrl}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="modal-footer">
            <button className="btn btn-secondary" onClick={onClose}>
              Cancelar
            </button>
            <button className="btn btn-primary" onClick={handleSave}>
              Guardar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

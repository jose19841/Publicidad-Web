// src/modules/users/components/UserActionsModal.jsx
import React, { useState, useEffect } from "react";

export default function UserActionsModal({ open, user, onClose, onSave }) {
  const [form, setForm] = useState({ email: "", role: "", enabled: true });

  useEffect(() => {
    if (user) setForm(user);
  }, [user]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(form);
  };

  if (!open) return null;
  return (
    <div className="modal d-block" tabIndex="-1">
      <div className="modal-dialog">
        <form className="modal-content" onSubmit={handleSubmit}>
          <div className="modal-header">
            <h5 className="modal-title">Editar Usuario</h5>
            <button type="button" className="btn-close" onClick={onClose} />
          </div>
          <div className="modal-body">
            <label className="form-label">Email</label>
            <input className="form-control mb-2" name="email" value={form.email} onChange={handleChange} required />
            <label className="form-label">Rol</label>
            <select className="form-select mb-2" name="role" value={form.role} onChange={handleChange}>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
            <div className="form-check">
              <input className="form-check-input" type="checkbox" name="enabled" checked={form.enabled} onChange={handleChange} />
              <label className="form-check-label">Activo</label>
            </div>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancelar</button>
            <button type="submit" className="btn btn-primary">Guardar</button>
          </div>
        </form>
      </div>
    </div>
  );
}

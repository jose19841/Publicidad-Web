// src/modules/users/components/UserActionsModal.jsx
import { useEffect, useState } from "react";
import Swal from "sweetalert2";
import { useAuth } from "../../../context/AuthContext";
import { updateAdminProfile, updateAdminUsernameMe } from "../services/adminService";

export default function UserActionsModal({ open, user, onClose, onSave }) {
  const [form, setForm] = useState({ id: null, email: "", role: "", enabled: true, username: "" });
  const { requireAuth, user: me } = useAuth();

  useEffect(() => {
    if (user) setForm(user);
  }, [user]);

  const isSelf = !!(me && form?.id && me.id === form.id);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await requireAuth();

      if (isSelf) {
        // Solo permitimos cambiar username del usuario logueado para no disparar el PUT que pisa el rol.
        const emailChanged = form.email && me?.email && form.email !== me.email;
        const roleChanged = form.role && me?.role && form.role !== me.role;
        const enabledChanged = typeof form.enabled === "boolean" && typeof me?.enabled === "boolean" && form.enabled !== me.enabled;

        if (emailChanged || roleChanged || enabledChanged) {
          await Swal.fire({
            icon: "warning",
            title: "Acción no permitida",
            text: "Para tu propio usuario, desde aquí solo podés cambiar el nombre de usuario. Email, rol y estado deben gestionarse por otro flujo.",
            confirmButtonColor: "#f5a524",
          });
          return;
        }

        await updateAdminUsernameMe(form.username || me.username);
      } else {
        // Actualizar OTRO usuario: el backend acepta UserRequestDTO (email, username, password).
        // Enviamos solo email y username para no interferir con el rol (el back igualmente lo pisa a USER).
        await updateAdminProfile(form.id, {
          email: form.email,
          username: form.username,
        });
      }

      await Swal.fire({
        icon: "success",
        title: "Datos guardados",
        text: "El usuario fue actualizado correctamente",
        confirmButtonColor: "#3085d6",
      });

      onSave && onSave(form);
      onClose && onClose();
    } catch (error) {
      await Swal.fire({
        icon: "error",
        title: "Error",
        text: error?.response?.data?.message || "No se pudo actualizar el usuario",
        confirmButtonColor: "#d33",
      });
    }
  };

  if (!open) return null;
  return (
    <div className="modal d-block" tabIndex="-1">
      <div className="modal-dialog">
        <form className="modal-content" onSubmit={handleSubmit}>
          <div className="modal-header">
            <h5 className="modal-title">Editar Usuario {isSelf ? "(tu usuario)" : ""}</h5>
            <button type="button" className="btn-close" onClick={onClose} />
          </div>
          <div className="modal-body">
            <label className="form-label">Nombre de usuario</label>
            <input
              className="form-control mb-2"
              name="username"
              value={form.username ?? ""}
              onChange={handleChange}
            />

            <label className="form-label">Email</label>
            <input
              className="form-control mb-2"
              name="email"
              value={form.email ?? ""}
              onChange={handleChange}
              required
              disabled={isSelf} // tu propio usuario no cambia email acá
            />

            <label className="form-label">Rol</label>
            <select
              className="form-select mb-2"
              name="role"
              value={form.role ?? ""}
              onChange={handleChange}
              disabled={isSelf} // tu propio usuario no cambia rol acá
            >
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>

            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="enabled"
                checked={!!form.enabled}
                onChange={handleChange}
                disabled={isSelf} // tu propio usuario no cambia enabled acá
              />
              <label className="form-check-label">Activo</label>
            </div>

            {isSelf && (
              <small className="text-muted d-block mt-2">
                Para evitar que el backend te cambie el rol, desde aquí solo se actualiza el <strong>nombre de usuario</strong>.
              </small>
            )}
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary">
              Guardar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

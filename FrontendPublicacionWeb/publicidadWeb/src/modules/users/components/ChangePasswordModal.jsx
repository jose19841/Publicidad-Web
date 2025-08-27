import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import Swal from "sweetalert2";
import { useAuth } from "../../../context/AuthContext";
import { useChangePassword } from "../hooks/useChangePassword";
import { changeAdminPassword } from "../services/adminService";

export default function ChangePasswordModal({ show, onClose, onSubmit }) {
  const {
    form, setField,
    showOld, setShowOld,
    showNew, setShowNew,
    showRepeat, setShowRepeat,
  } = useChangePassword(show);

  const { requireAuth } = useAuth();
  const [loading, setLoading] = useState(false);

  if (!show) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();

    if ((form.newPassword ?? "").length < 8) {
      await Swal.fire({
        icon: "warning",
        title: "Contraseña demasiado corta",
        text: "La nueva contraseña debe tener al menos 8 caracteres.",
      });
      return;
    }
    if (form.newPassword !== form.repeatPassword) {
      await Swal.fire({
        icon: "warning",
        title: "Las contraseñas no coinciden",
        text: "Revisá que la nueva contraseña y su repetición sean iguales.",
      });
      return;
    }

    try {
      setLoading(true);
      await requireAuth();
      await changeAdminPassword({ ...form });

      await Swal.fire({
        icon: "success",
        title: "Contraseña cambiada",
        text: "Tu contraseña fue actualizada correctamente",
        confirmButtonColor: "#3085d6",
      });

      onSubmit && onSubmit({ ...form });
      onClose && onClose();
    } catch (error) {
      await Swal.fire({
        icon: "error",
        title: "Error",
        text: error?.response?.data?.error || "No se pudo cambiar la contraseña",
        confirmButtonColor: "#d33",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="modal fade show d-block" tabIndex="-1" role="dialog" aria-modal="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content rounded-3">
            <div className="modal-header">
              <h5 className="modal-title">Cambiar contraseña</h5>
              <button type="button" className="btn-close" aria-label="Close" onClick={onClose} disabled={loading}></button>
            </div>

            <div className="modal-body">
              <form className="d-grid gap-3" onSubmit={handleSubmit}>
                {/* Contraseña actual */}
                <div className="input-group input-group-lg">
                  <input
                    type={showOld ? "text" : "password"}
                    className="form-control"
                    placeholder="Contraseña actual"
                    value={form.currentPassword}
                    onChange={(e) => setField("currentPassword", e.target.value)}
                    required
                    disabled={loading}
                  />
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setShowOld(v => !v)}
                    disabled={loading}
                  >
                    {showOld ? <FaEyeSlash className="icon-eye" /> : <FaEye className="icon-eye" />}
                  </button>
                </div>

                {/* Nueva contraseña */}
                <div className="input-group input-group-lg">
                  <input
                    type={showNew ? "text" : "password"}
                    className="form-control"
                    placeholder="Nueva contraseña (mín. 8 caracteres)"
                    value={form.newPassword}
                    onChange={(e) => setField("newPassword", e.target.value)}
                    required
                    minLength={8}
                    disabled={loading}
                  />
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setShowNew(v => !v)}
                    disabled={loading}
                  >
                    {showNew ? <FaEyeSlash className="icon-eye" /> : <FaEye className="icon-eye" />}
                  </button>
                </div>

                {/* Repetir nueva */}
                <div className="input-group input-group-lg">
                  <input
                    type={showRepeat ? "text" : "password"}
                    className="form-control"
                    placeholder="Repetir nueva contraseña"
                    value={form.repeatPassword}
                    onChange={(e) => setField("repeatPassword", e.target.value)}
                    required
                    minLength={8}
                    disabled={loading}
                  />
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setShowRepeat(v => !v)}
                    disabled={loading}
                  >
                    {showRepeat ? <FaEyeSlash className="icon-eye" /> : <FaEye className="icon-eye" />}
                  </button>
                </div>

                <button type="submit" className="btn btn-primary btn-lg w-100" disabled={loading}>
                  {loading ? "Cambiando..." : "Cambiar contraseña"}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>

      <div className="modal-backdrop fade show"></div>
    </>
  );
}

import { useEffect, useState } from "react";
import Swal from "sweetalert2";

export default function UserProfileModal({
  show,
  onClose,
  user,                   // { username, email }
  onSubmit,               // (payload: { username }) => void
  onOpenChangePassword,   // () => void
}) {
  const [username, setUsername] = useState(user?.username ?? "");

  useEffect(() => {
    if (show) {
      setUsername(user?.username ?? "");
      document.body.classList.add("modal-open");
    } else {
      document.body.classList.remove("modal-open");
    }
    return () => document.body.classList.remove("modal-open");
  }, [show, user?.username]);

  if (!show) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    const trimmed = (username ?? "").trim();

    // Validación: permitir espacios, longitud 4–20
    if (trimmed.length < 4 || trimmed.length > 20) {
      await Swal.fire({
        icon: "warning",
        title: "Nombre inválido",
        text: "El nombre de usuario debe tener entre 4 y 20 caracteres.",
      });
      return;
    }

    onSubmit?.({ username: trimmed });
  };

  return (
    <>
      <div className="modal fade show d-block" tabIndex="-1" role="dialog" aria-modal="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content rounded-3">
            <div className="modal-header">
              <h5 className="modal-title">Editar perfil</h5>
              <button type="button" className="btn-close" aria-label="Close" onClick={onClose}></button>
            </div>

            <div className="modal-body">
              <form className="d-grid gap-3" onSubmit={handleSubmit}>
                <div>
                  <label className="form-label">Nombre de usuario *</label>
                  <input
                    className="form-control form-control-lg"
                    placeholder="Tu usuario"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    minLength={4}
                    maxLength={20}
                  />
                  <div className="form-text">Podés usar espacios (ej: "Jose Perez").</div>
                </div>

                <div>
                  <label className="form-label">Correo electrónico</label>
                  <input
                    className="form-control form-control-lg"
                    value={user?.email ?? ""}
                    readOnly
                  />
                </div>

                <button
                  type="button"
                  className="btn btn-outline-primary btn-lg"
                  onClick={() => {
                    onClose?.();
                    onOpenChangePassword?.();
                  }}
                >
                  CAMBIAR CONTRASEÑA
                </button>

                <button type="submit" className="btn btn-primary btn-lg">
                  GUARDAR CAMBIOS
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

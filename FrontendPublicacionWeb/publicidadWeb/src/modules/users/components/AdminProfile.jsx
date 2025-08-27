// src/users/components/AdminProfile.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { useAuth } from "../../../context/AuthContext"; // <- no tocar
import { useAdminProfile } from "../hooks/useAdminProfile";
import { getUserSession, updateAdminUsernameMe } from "../services/adminService.js";
import ChangePasswordModal from "./ChangePasswordModal";

export default function AdminProfile({ initialName = "admin", initialEmail = "admin@admin.com" }) {
  const navigate = useNavigate();

  // 🔹 Tomamos el usuario REAL del AuthContext para iniciar el form con su nombre/email.
  //    Si no hay user aún, cae en los valores por defecto recibidos por props.
  const { user: authUser, login } = useAuth();

  const {
    name, setName,
    email, setEmail,
    showPwd, openPwd, closePwd,
  } = useAdminProfile({
    initialName: authUser?.username ?? initialName,
    initialEmail: authUser?.email ?? initialEmail,
  });

  const [saving, setSaving] = useState(false);

  const handleSave = async (e) => {
    e?.preventDefault?.();

    const trimmed = (name ?? "").trim();
    if (trimmed.length < 4 || trimmed.length > 20) {
      Swal.fire({
        icon: "warning",
        title: "Nombre inválido",
        text: "El nombre de usuario debe tener entre 4 y 20 caracteres.",
      });
      return;
    }

    try {
      setSaving(true);
      await updateAdminUsernameMe(trimmed); // envía { username, email(actual) }

      // refresca el contexto para que el sidebar se actualice sin F5
      try {
        const fresh = await getUserSession();
        login(fresh);
      } catch {}

      Swal.fire({
        icon: "success",
        title: "Guardado",
        text: "Usuario actualizado correctamente.",
        timer: 1600,
        showConfirmButton: false,
      });
    } catch (err) {
      const msg = err?.response?.data || "No se pudo actualizar el usuario.";
      Swal.fire({
        icon: "error",
        title: "Error",
        text: String(msg),
      });
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="container py-4">
      {/* Encabezado */}
      <div className="text-center mb-4">
        <h1 className="display-5 fw-semibold mb-1">{name || "admin"}</h1>
        <div className="text-uppercase text-muted fw-semibold">ADMIN</div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSave} className="mx-auto" style={{ maxWidth: 560 }}>
        <div className="mb-3">
          <label className="form-label">Nombre de usuario *</label>
          <input
            className="form-control form-control-lg"
            placeholder="admin"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>

        <div className="mb-4">
          <label className="form-label">Correo electrónico *</label>
          <input
            type="email"
            className="form-control form-control-lg"
            placeholder="admin@admin.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            readOnly
            required
          />
        </div>

        <div className="d-grid gap-3">
          <button type="button" className="btn btn-primary btn-lg" onClick={openPwd}>
            CAMBIAR CONTRASEÑA
          </button>

          <button type="submit" className="btn btn-primary btn-lg" disabled={saving}>
            {saving ? "GUARDANDO..." : "GUARDAR CAMBIOS"}
          </button>

          <button type="button" className="btn btn-primary btn-lg" onClick={() => navigate(-1)}>
            VOLVER
          </button>
        </div>
      </form>

      {/* Modal Cambiar contraseña */}
      <ChangePasswordModal
        show={showPwd}
        onClose={closePwd}
        onSubmit={() => {
          closePwd();
        }}
      />
    </div>
  );
}

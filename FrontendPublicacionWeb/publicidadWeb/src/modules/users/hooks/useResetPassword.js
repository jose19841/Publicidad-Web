import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import resetPasswordService from "../services/resetPasswordService";

export default function useResetPasswordForm({ token }) {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPass, setShowPass] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [touched, setTouched] = useState(false);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState("");

  const navigate = useNavigate();

  // validaciones dinámicas
  const passwordError =
    touched && !newPassword
      ? "La contraseña es obligatoria"
      : touched && newPassword.length < 6
      ? "La contraseña debe tener al menos 6 caracteres"
      : "";

  const confirmError =
    touched && confirmPassword !== newPassword
      ? "Las contraseñas no coinciden"
      : "";

  /** 
   * Verifica todas las validaciones antes de enviar 
   */
  const validateForm = () => {
    if (!newPassword || newPassword.length < 6) return false;
    if (confirmPassword !== newPassword) return false;
    return true;
  };

  const submit = async (e) => {
    e.preventDefault();
    setTouched(true);

    if (!validateForm()) {
      setError("Revisá los errores en el formulario antes de continuar.");
      return;
    }

    setLoading(true);
    setError(null);
    setSuccessMsg("");
    try {
      await resetPasswordService({ token, newPassword });
      setSuccessMsg(
        "La contraseña fue restablecida correctamente. Serás redirigido en unos segundos."
      );
      setNewPassword("");
      setConfirmPassword("");
    } catch (e) {
      setError("Enlace expirado");
    } finally {
      setLoading(false);
    }
  };

  // Redirigir tras éxito (más tiempo)
  useEffect(() => {
    if (successMsg) {
      const timer = setTimeout(() => navigate("/login"), 8000); // 8 seg
      return () => clearTimeout(timer);
    }
  }, [successMsg, navigate]);

  return {
    newPassword,
    setNewPassword,
    confirmPassword,
    setConfirmPassword,
    showPass,
    setShowPass,
    showConfirm,
    setShowConfirm,
    passwordError,
    confirmError,
    loading,
    error,
    successMsg,
    submit,
  };
}

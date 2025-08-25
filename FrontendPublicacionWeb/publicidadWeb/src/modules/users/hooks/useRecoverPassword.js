import { useState } from "react";
import forgotPasswordService from "../services/forgotPasswordService"

/**
 * Hook para solicitar el envío del token de recuperación.
 * Maneja email, loading, error y successMsg.
 */
export default function useForgotPassword() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState("");

  const submit = async () => {
    setLoading(true);
    setError(null);
    setSuccessMsg("");
    try {
      await forgotPasswordService({ email });
      setSuccessMsg(
        "Si el correo está registrado, te enviamos un enlace para restablecer tu contraseña. Revisá tu bandeja de entrada y el spam."
      );
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    email,
    setEmail,
    loading,
    error,
    successMsg,
    submit,
  };
}

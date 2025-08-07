import { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";
import Swal from "sweetalert2";

const useRegister = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const validateForm = (email, username, password, confirmPassword) => {
    if (!email || !/\S+@\S+\.\S+/.test(email)) {
      return "Ingrese un email válido.";
    }
    if (!username || username.length < 4 || username.length > 20) {
      return "El nombre de usuario debe tener entre 4 y 20 caracteres.";
    }
    if (!password || password.length < 8) {
      return "La contraseña debe tener al menos 8 caracteres.";
    }
    if (password !== confirmPassword) {
      return "Las contraseñas no coinciden.";
    }
    return null;
  };

  const handleRegister = async (email, username, password, confirmPassword) => {
    setLoading(true);
    setError("");
    const validationError = validateForm(email, username, password, confirmPassword);
    if (validationError) {
      setLoading(false);
      setError(validationError);
      return null;
    }

    try {
      await authService.register({ email, username, password });

      // ✅ Mostrar SweetAlert al éxito
      Swal.fire({
        icon: "success",
        title: "¡Registro exitoso!",
        text: "Ahora puedes iniciar sesión con tus credenciales.",
        confirmButtonText: "Ir al login",
      }).then(() => {
        navigate("/login"); // Redirige después de aceptar
      });

    } catch (err) {
      setError(err.response?.data?.error || "Error al registrar usuario.");
      return null;
    } finally {
      setLoading(false);
    }
  };

  return { loading, error, handleRegister };
};

export default useRegister;

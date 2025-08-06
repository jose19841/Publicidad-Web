import { useState } from "react";
import authService from "../services/authService";

const useLogin = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const validateForm = (email, password) => {
    if (!email || !/\S+@\S+\.\S+/.test(email)) {
      return "Ingrese un email válido.";
    }
    if (!password || password.length < 6) {
      return "La contraseña debe tener al menos 6 caracteres.";
    }
    return null;
  };

  const handleLogin = async (email, password, rememberMe) => {
    setLoading(true);
    setError("");
    const validationError = validateForm(email, password);
    if (validationError) {
      setLoading(false);
      setError(validationError);
      return null;
    }

    try {
      const user = await authService.login({ email, password, rememberMe });
      alert('login exitoso')
      return user;
    } catch (err) {
      setError(err.response?.data?.error || "Error al iniciar sesión.");
      return null;
    } finally {
      setLoading(false);
    }
  };

  return { loading, error, handleLogin };
};

export default useLogin;

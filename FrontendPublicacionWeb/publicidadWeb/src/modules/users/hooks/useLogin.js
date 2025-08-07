import { useState } from "react";
import authService from "../services/authService";
import { useAuth } from "../../../context/AuthContext"; 
const useLogin = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { login } = useAuth(); // <-- Usá el método del contexto

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
      login(user); // <-- Guarda el user en el contexto
      // alert('login exitoso'); // Mejor usá un SweetAlert, o hacé la redirección desde el componente
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

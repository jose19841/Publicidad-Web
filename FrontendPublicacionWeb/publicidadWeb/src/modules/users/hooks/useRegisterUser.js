import { useState } from "react";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../services/userService";
import validateUserForm from "../validation/validateUserForm";

export default function useRegisterUser() {
  const [form, setForm] = useState({
    email: "",
    username: "",
    password: "",
    repeatPassword: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const [touched, setTouched] = useState({});
  const navigate = useNavigate();

  // Validaciones afuera del componente
  const validation = validateUserForm(form);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
    setError("");
  };

  const handleBlur = (e) => {
    setTouched((prev) => ({ ...prev, [e.target.name]: true }));
  };

  // Limpia touched si el form está vacío (opcional)
  if (
    form.email === "" &&
    form.username === "" &&
    form.password === "" &&
    form.repeatPassword === "" &&
    Object.keys(touched).length !== 0
  ) {
    setTouched({});
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (Object.keys(validation).length > 0) {
      // Marca todos como tocados para mostrar errores
      setTouched({
        email: true,
        username: true,
        password: true,
        repeatPassword: true,
      });
      return;
    }
    setLoading(true);
    try {
      await registerUser({
        email: form.email,
        username: form.username,
        password: form.password,
      });
      setForm({
        email: "",
        username: "",
        password: "",
        repeatPassword: "",
      });
      setTouched({});
      await Swal.fire({
        title: "¡Registro exitoso!",
        text: "Usuario creado correctamente. Ahora puedes iniciar sesión.",
        icon: "success",
        confirmButtonText: "Ir al login",
        allowOutsideClick: false,
        allowEscapeKey: false,
      });
      navigate("/login");
    } catch (err) {
      setError(
        err?.response?.data?.error ||
        err?.response?.data?.message ||
        err?.message ||
        "Error al registrar usuario"
      );
    } finally {
      setLoading(false);
    }
  };

  return {
    form,
    handleChange,
    handleSubmit,
    handleBlur,
    touched,
    setTouched,
    loading,
    error,
    validation,
  };
}

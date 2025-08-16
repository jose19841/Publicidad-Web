import { useState } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Stack from "@mui/material/Stack";
import Alert from "@mui/material/Alert";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import useRecoverPassword from "../hooks/useRecoverPassword";
import "../styles/RecoverPassword.css";
import { Link, Navigate } from "react-router-dom";

export default function RecoverPasswordForm() {
  const { email, setEmail, loading, error, successMsg, submit } =
    useRecoverPassword();
  const [touched, setTouched] = useState(false);

  const emailError =
    touched && !email
      ? "El correo es obligatorio"
      : touched && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
      ? "Ingresá un correo válido"
      : "";

  const handleSubmit = async (e) => {
    e.preventDefault();
    setTouched(true);
    if (emailError) return;
    await submit();
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="auth-form">
      <Stack spacing={2}>
        {successMsg && (
          <Alert
            icon={<CheckCircleIcon fontSize="inherit" />}
            severity="success"
          >
            {successMsg}
          </Alert>
        )}
        {error && <Alert severity="error">{error}</Alert>}

        <TextField
          label="Correo electrónico"
          type="email"
          name="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          onBlur={() => setTouched(true)}
          error={Boolean(emailError)}
          helperText={emailError || "Ej.: usuario@dominio.com"}
          fullWidth
          disabled={loading}
          autoComplete="email"
        />

        <Button
          type="submit"
          variant="contained"
          size="large"
          disabled={loading}
          fullWidth
          className="auth-button"
        >
          {loading ? "Enviando..." : "Enviar enlace de recuperación"}
        </Button>
      </Stack>

      <div className="mt-3">
        <Link to="/login" style={{ textDecoration: "none" }}>
          Volver al inicio de sesión
        </Link>
      </div>
    </form>
  );
}

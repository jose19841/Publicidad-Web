import { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import useLogin from "../hooks/useLogin";
import authService from "../services/authService";

import Box from "@mui/material/Box";
import Link from "@mui/material/Link";
import Button from "@mui/material/Button";
import Divider from "@mui/material/Divider";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import InputAdornment from "@mui/material/InputAdornment";
import Checkbox from "@mui/material/Checkbox";
import FormControlLabel from "@mui/material/FormControlLabel";
import { Icon } from "@iconify/react";
import { useAuth } from "../../../context/AuthContext";
/**
 * LoginForm reutilizable.
 * - disableNavigate: si es true, no hace navigate() y usa onSuccess.
 * - onSuccess: callback al loguear correctamente (para portal).
 */
const LoginForm = ({ onSuccess, disableNavigate = false }) => {
  const { login } = useAuth();
  const [email, setEmail]       = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { loading, error, handleLogin } = useLogin();
  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    const user = await handleLogin(email, password, rememberMe);
    if (!user) return;

    if (disableNavigate || onSuccess) {
      onSuccess?.(user);
      return;
    }

    if (user.role === "ADMIN")      navigate("/admin/dashboard");
    else if (user.role === "USER")  navigate("/publicaciones");
    else                            navigate("/");
  };

  // Popup de Google OAuth
  const openGooglePopup = () => {
    const w = 520, h = 640;
    const dualScreenLeft = window.screenLeft ?? window.screenX ?? 0;
    const dualScreenTop  = window.screenTop  ?? window.screenY ?? 0;
    const left = dualScreenLeft + (window.outerWidth - w) / 2;
    const top  = dualScreenTop + (window.outerHeight - h) / 2;

    const popup = window.open(
      "http://localhost:8080/oauth2/authorization/google",
      "google_oauth",
      `toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=${w},height=${h},top=${top},left=${left}`
    );
    if (!popup) return;

    const timer = setInterval(async () => {
      if (popup.closed) {
        clearInterval(timer);
        return;
      }
      try {
        const me = await authService.getProfile(); // con withCredentials: true
        if (me) {
          clearInterval(timer);
          try { popup.close(); } catch {}
          login(me)
          if (disableNavigate || onSuccess) {
            onSuccess?.(me);
            return;
          }
          if (me.role === "ADMIN")      navigate("/admin/dashboard");
          else if (me.role === "USER")  navigate("/publicaciones");
          else                          navigate("/");
        }
      } catch {
        // No hay sesión todavía
      }
    }, 800);
  };

  return (
    <Box sx={{ maxWidth: 400, margin: "auto" }}>
      <Box sx={{ gap: 1.5, display: "flex", flexDirection: "column", alignItems: "center", mb: 5 }}>
        <Typography variant="h5">Iniciar Sesión</Typography>
        <Typography variant="body2" sx={{ color: "text.secondary" }}>
          ¿No tienes una cuenta?
          <Link component={RouterLink} to="/register" variant="subtitle2" sx={{ ml: 0.5 }}>
            Regístrate
          </Link>
        </Typography>
      </Box>

      <Box component="form" onSubmit={onSubmit}>
        <TextField
          fullWidth
          label="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          sx={{ mb: 3 }}
          InputLabelProps={{ shrink: true }}
        />

        <TextField
          fullWidth
          label="Contraseña"
          type={showPassword ? "text" : "password"}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          InputLabelProps={{ shrink: true }}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                  <Icon icon={showPassword ? "solar:eye-bold" : "solar:eye-closed-bold"} />
                </IconButton>
              </InputAdornment>
            ),
          }}
          sx={{ mb: 2 }}
        />

        <FormControlLabel
          control={<Checkbox checked={rememberMe} onChange={(e) => setRememberMe(e.target.checked)} />}
          label="Recuérdame"
        />

        {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}

        <Button
          fullWidth
          size="large"
          type="submit"
          variant="contained"
          color="primary"
          disabled={loading}
        >
          {loading ? "Ingresando..." : "Ingresar"}
        </Button>
      </Box>

      <Divider sx={{ my: 3, "&::before, &::after": { borderTopStyle: "dashed" } }}>
        <Typography variant="overline" sx={{ color: "text.secondary", fontWeight: "fontWeightMedium" }}>
          O
        </Typography>
      </Divider>

      <Box sx={{ gap: 1, display: "flex", justifyContent: "center" }}>
        <Typography variant="overline" sx={{ color: "text.secondary", fontWeight: "fontWeightMedium" }}>
          inicia sesión con
        </Typography>
        <IconButton color="inherit" onClick={openGooglePopup}>
          <Icon icon="logos:google-icon" width={25} style={{ marginTop: -7 }} />
        </IconButton>
      </Box>
    </Box>
  );
};

export default LoginForm;

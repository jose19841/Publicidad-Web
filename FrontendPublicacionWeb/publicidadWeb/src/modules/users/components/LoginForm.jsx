import { useState } from "react";
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
import { useNavigate } from "react-router-dom";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { loading, error, handleLogin } = useLogin();
  const navigate = useNavigate();
  const onSubmit = async (e) => {
    e.preventDefault();
    const user = await handleLogin(email, password, rememberMe);
    if (user) {
      if (user.role === "ADMIN") {
        navigate("/admin/dashboard"); // O la ruta real de tu dashboard admin
      } else if (user.role === "USER") {
        navigate("/publicaciones"); // O la ruta real para usuarios normales
      } else {
        navigate("/"); // fallback, por si algún otro rol inesperado
      }
    }
  };

  return (
    <Box sx={{ maxWidth: 400, margin: "auto" }}>
      <Box
        sx={{
          gap: 1.5,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          mb: 5,
        }}
      >
        <Typography variant="h5">Iniciar Sesión</Typography>
        <Typography variant="body2" sx={{ color: "text.secondary" }}>
          ¿No tienes una cuenta?
          <Link variant="subtitle2" sx={{ ml: 0.5 }} href="/register">
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
                <IconButton
                  onClick={() => setShowPassword(!showPassword)}
                  edge="end"
                >
                  <Icon
                    icon={
                      showPassword ? "solar:eye-bold" : "solar:eye-closed-bold"
                    }
                  />
                </IconButton>
              </InputAdornment>
            ),
          }}
          sx={{ mb: 2 }}
        />

        <FormControlLabel
          control={
            <Checkbox
              checked={rememberMe}
              onChange={(e) => setRememberMe(e.target.checked)}
            />
          }
          label="Recuérdame"
        />

        {error && (
          <Typography color="error" sx={{ mb: 2 }}>
            {error}
          </Typography>
        )}

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

      <Divider
        sx={{ my: 3, "&::before, &::after": { borderTopStyle: "dashed" } }}
      >
        <Typography
          variant="overline"
          sx={{ color: "text.secondary", fontWeight: "fontWeightMedium" }}
        >
          O
        </Typography>
      </Divider>

      <Box sx={{ gap: 1, display: "flex", justifyContent: "center" }}>
        <IconButton
          color="inherit"
          onClick={() => authService.socialLogin("google")}
        >
          <Icon width={22} icon="logos:google-icon" />
        </IconButton>
        <IconButton
          color="inherit"
          onClick={() => authService.socialLogin("facebook")}
        >
          <Icon width={22} icon="mdi:facebook" />
        </IconButton>
      </Box>
    </Box>
  );
};

export default LoginForm;

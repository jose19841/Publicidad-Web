import { useState } from "react";
import useRegister from "../hooks/useRegister";

import Box from "@mui/material/Box";
import Link from "@mui/material/Link";
import Button from "@mui/material/Button";
import Divider from "@mui/material/Divider";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import InputAdornment from "@mui/material/InputAdornment";
import { Icon } from "@iconify/react";

const RegisterForm = () => {
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const { loading, error, handleRegister } = useRegister();

  const onSubmit = async (e) => {
    e.preventDefault();
    const user = await handleRegister(email, username, password, confirmPassword);
    if (user) {
      console.log("Registro exitoso:", user);
      // Podés redirigir al login o dashboard
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
        <Typography variant="h5">Crear cuenta</Typography>
        <Typography variant="body2" sx={{ color: "text.secondary" }}>
          ¿Ya tienes cuenta?
          <Link variant="subtitle2" sx={{ ml: 0.5 }} href="/login">
            Inicia sesión
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
          label="Nombre de usuario"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
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
          sx={{ mb: 3 }}
        />

        <TextField
          fullWidth
          label="Repetir contraseña"
          type={showPassword ? "text" : "password"}
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          InputLabelProps={{ shrink: true }}
          sx={{ mb: 2 }}
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
          {loading ? "Registrando..." : "Registrarse"}
        </Button>
      </Box>

      <Divider sx={{ my: 3, "&::before, &::after": { borderTopStyle: "dashed" } }}>
        <Typography
          variant="overline"
          sx={{ color: "text.secondary", fontWeight: "fontWeightMedium" }}
        >
          O
        </Typography>
      </Divider>

      <Box sx={{ gap: 1, display: "flex", justifyContent: "center" }}>
        <IconButton color="inherit">
          <Icon width={22} icon="logos:google-icon" />
        </IconButton>
        <IconButton color="inherit">
          <Icon width={22} icon="mdi:facebook" />
        </IconButton>
      </Box>
    </Box>
  );
};

export default RegisterForm;

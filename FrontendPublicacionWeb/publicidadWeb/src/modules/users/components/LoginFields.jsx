import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Checkbox from "@mui/material/Checkbox";
import FormControlLabel from "@mui/material/FormControlLabel";
import Typography from "@mui/material/Typography";
import Alert from "@mui/material/Alert";
import { Icon } from "@iconify/react";

export default function LoginFields({
  email, setEmail,
  password, setPassword,
  showPassword, setShowPassword,
  rememberMe, setRememberMe,
  error
}) {
  return (
    <Box
      sx={{
        p: 2.5,
        border: "1px solid",
        borderColor: "divider",
        borderRadius: 2,
        boxShadow: 1,
        display: "flex",
        flexDirection: "column",
        gap: 2,
        animation: "fadeIn 0.6s ease-in-out",
        "@keyframes fadeIn": {
          from: { opacity: 0, transform: "translateY(-10px)" },
          to: { opacity: 1, transform: "translateY(0)" }
        }
      }}
    >
      <TextField
        fullWidth
        label="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        variant="outlined"
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:letter-bold" width={22} style={{ opacity: 0.8 }} />
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": {
            borderRadius: 2
          }
        }}
      />

      <TextField
        fullWidth
        label="Contraseña"
        type={showPassword ? "text" : "password"}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        variant="outlined"
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:lock-bold" width={22} style={{ opacity: 0.8 }} />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={() => setShowPassword(!showPassword)} edge="end" aria-label="Mostrar/ocultar contraseña">
                <Icon icon={showPassword ? "solar:eye-bold" : "solar:eye-closed-bold"} width={22} />
              </IconButton>
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": {
            borderRadius: 2
          }
        }}
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={rememberMe}
            onChange={(e) => setRememberMe(e.target.checked)}
          />
        }
        label="Recuérdame"
        sx={{ mt: 0.5 }}
      />

      {error && (
        <Alert severity="error" variant="outlined" sx={{ borderRadius: 2 }}>
          <Typography variant="body2">{error}</Typography>
        </Alert>
      )}
    </Box>
  );
}

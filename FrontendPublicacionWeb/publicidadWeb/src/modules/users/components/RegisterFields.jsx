import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Typography from "@mui/material/Typography";
import Alert from "@mui/material/Alert";
import { Icon } from "@iconify/react";

export default function RegisterFields({
  email, setEmail,
  username, setUsername,
  password, setPassword,
  confirmPassword, setConfirmPassword,
  showPassword, setShowPassword,
  error,
  size = "small" // compacta por defecto
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
      {/* Email */}
      <TextField
        fullWidth
        label="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        variant="outlined"
        size={size}
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:letter-bold" width={20} style={{ opacity: 0.85 }} />
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": { borderRadius: 2 }
        }}
      />

      {/* Usuario */}
      <TextField
        fullWidth
        label="Nombre de usuario"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        variant="outlined"
        size={size}
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:user-bold" width={20} style={{ opacity: 0.85 }} />
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": { borderRadius: 2 }
        }}
      />

      {/* Contraseña */}
      <TextField
        fullWidth
        label="Contraseña"
        type={showPassword ? "text" : "password"}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        variant="outlined"
        size={size}
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:lock-bold" width={20} style={{ opacity: 0.85 }} />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                onClick={() => setShowPassword(!showPassword)}
                edge="end"
                aria-label="Mostrar u ocultar contraseña"
              >
                <Icon icon={showPassword ? "solar:eye-bold" : "solar:eye-closed-bold"} width={20} />
              </IconButton>
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": { borderRadius: 2 }
        }}
      />

      {/* Repetir contraseña */}
      <TextField
        fullWidth
        label="Repetir contraseña"
        type={showPassword ? "text" : "password"}
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
        variant="outlined"
        size={size}
        InputLabelProps={{ shrink: true }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="solar:lock-keyhole-bold" width={20} style={{ opacity: 0.85 }} />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                onClick={() => setShowPassword(!showPassword)}
                edge="end"
                aria-label="Mostrar u ocultar contraseña"
              >
                <Icon icon={showPassword ? "solar:eye-bold" : "solar:eye-closed-bold"} width={20} />
              </IconButton>
            </InputAdornment>
          )
        }}
        sx={{
          "& .MuiOutlinedInput-root": { borderRadius: 2 }
        }}
      />

      {/* Error */}
      {error && (
        <Alert severity="error" variant="outlined" sx={{ borderRadius: 2 }}>
          <Typography variant="body2">{error}</Typography>
        </Alert>
      )}
    </Box>
  );
}

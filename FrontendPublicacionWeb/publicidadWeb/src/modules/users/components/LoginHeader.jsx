import { Link as RouterLink } from "react-router-dom";
import Box from "@mui/material/Box";
import Link from "@mui/material/Link";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import LoginIcon from "@mui/icons-material/Login";

export default function LoginHeader() {
  return (
    <Box
      sx={{
        gap: 1.5,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        mb: 1,  
        textAlign: "center",
        animation: "fadeIn 0.6s ease-in-out",
        "@keyframes fadeIn": {
          from: { opacity: 0, transform: "translateY(-10px)" },
          to: { opacity: 1, transform: "translateY(0)" }
        }
      }}
    >
     

      {/* Título con gradiente */}
      <Typography
        variant="h4"
        sx={{
          fontWeight: "bold",
          background: "linear-gradient(90deg, #1976d2, #42a5f5)",
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent"
        }}
      >
        {/* Icono */}
      <LoginIcon sx={{ fontSize: 48, color: "primary.main" }} /> Iniciar Sesión
      </Typography>

      {/* Separador decorativo */}
      <Divider sx={{ width: 60, borderBottomWidth: 3, borderColor: "primary.main" }} />

      {/* Texto registro */}
      <Typography variant="body2" sx={{ color: "text.secondary" }}>
        ¿No tienes una cuenta?
        <Link
          component={RouterLink}
          to="/register"
          variant="subtitle2"
          sx={{
            ml: 0.5,
            fontWeight: "bold",
            color: "primary.main",
            "&:hover": { textDecoration: "underline" }
          }}
        >
          Regístrate
        </Link>
      </Typography>
    </Box>
  );
}

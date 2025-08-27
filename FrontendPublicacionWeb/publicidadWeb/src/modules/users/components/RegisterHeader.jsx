import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import Divider from "@mui/material/Divider";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1"; // Icono de registro

export default function RegisterHeader() {
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
          background: "linear-gradient(90deg, #2e7d32, #66bb6a)",
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent"
        }}
      >
         {/* Icono */}
      <PersonAddAlt1Icon sx={{ fontSize: 48, color: "primary.main" }} />  Crear cuenta
      </Typography>

      {/* Separador decorativo */}
      <Divider sx={{ width: 60, borderBottomWidth: 3, borderColor: "primary.main" }} />

      {/* Texto con enlace */}
      <Typography variant="body2" sx={{ color: "text.secondary" }}>
        ¿Ya tienes cuenta?
        <Link
          href="/login"
          variant="subtitle2"
          sx={{
            ml: 0.5,
            fontWeight: "bold",
            color: "primary.main",
            "&:hover": { textDecoration: "underline" }
          }}
        >
          Inicia sesión
        </Link>
      </Typography>
    </Box>
  );
}

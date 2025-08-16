import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import { Icon } from "@iconify/react";

export default function LoginSocial({ openGooglePopup }) {
  return (
    <Box
      sx={{
        mt: 1,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        gap: 1,
        textAlign: "center",
        animation: "fadeIn 0.4s ease-in-out",
        "@keyframes fadeIn": {
          from: { opacity: 0, transform: "translateY(-5px)" },
          to: { opacity: 1, transform: "translateY(0)" }
        }
      }}
    >
      <Typography
        variant="body2"
        sx={{
          color: "text.secondary",
          fontWeight: 500
        }}
      >
        Inicia sesión con
      </Typography>

      <IconButton
        aria-label="Iniciar sesión con Google"
        sx={{
          backgroundColor: "#fff",
          border: "1px solid #ddd",
          p: 1,
          borderRadius: "50%",
          transition: "all 0.3s ease",
          "&:hover": {
            transform: "scale(1.05)",
            boxShadow: "0 3px 8px rgba(0,0,0,0.2)"
          }
        }}
        onClick={openGooglePopup}
      >
        <Icon icon="logos:google-icon" width={22} />
      </IconButton>
    </Box>
  );
}

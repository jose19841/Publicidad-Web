import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import RecoverPasswordForm from "../components/RecoverPasswordForm";
import "../styles/RecoverPassword.css"; 

export default function RecoverPasswordPage() {
  return (
    <Box className="auth-page">
      <Container maxWidth="sm">
        <Paper className="auth-card">
          <Typography variant="h5" fontWeight={700} gutterBottom>
            Recuperar contraseña
          </Typography>
          <Typography variant="body2" color="text.secondary" className="auth-subtitle">
            Ingresá tu correo electrónico. Si existe una cuenta asociada, te enviaremos un enlace para restablecer tu contraseña.
          </Typography>
          <RecoverPasswordForm />
        </Paper>
      </Container>
    </Box>
  );
}

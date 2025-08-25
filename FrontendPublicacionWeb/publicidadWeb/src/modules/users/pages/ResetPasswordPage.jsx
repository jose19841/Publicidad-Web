import { useSearchParams } from "react-router-dom";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import ResetPasswordForm from "../components/ResetPasswordForm";
import "../styles/RecoverPassword.css"; 

export default function ResetPasswordPage() {
  const [params] = useSearchParams();
  const token = params.get("token");

  return (
    <Box className="auth-page">
      <Container maxWidth="sm">
        <Paper className="auth-card">
          <Typography variant="h5" fontWeight={700} gutterBottom>
            Restablecer contraseña
          </Typography>
          <Typography variant="body2" color="text.secondary" className="auth-subtitle">
            Ingresá tu nueva contraseña y confirmala. 
          </Typography>
          <ResetPasswordForm token={token} />
        </Paper>
      </Container>
    </Box>
  );
}

import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";
import Typography from "@mui/material/Typography";
import useRegisterForm from "../hooks/useRegisterForm";
import RegisterHeader from "./RegisterHeader";
import RegisterFields from "./RegisterFields";
import RegisterDivider from "./RegisterDivider";
import RegisterSocial from "./RegisterSocial";

export default function RegisterForm() {
  const {
    email, setEmail,
    username, setUsername,
    password, setPassword,
    confirmPassword, setConfirmPassword,
    showPassword, setShowPassword,
    loading, error,
    onSubmit
  } = useRegisterForm();

  return (
    <div>

 
        <RegisterHeader />

        <Box
          component="form"
          onSubmit={onSubmit}
          sx={{ display: "grid", gap: 1.5 }}
        >
          <RegisterFields
            email={email}
            setEmail={setEmail}
            username={username}
            setUsername={setUsername}
            password={password}
            setPassword={setPassword}
            confirmPassword={confirmPassword}
            setConfirmPassword={setConfirmPassword}
            showPassword={showPassword}
            setShowPassword={setShowPassword}
            error={error}
            size="small"
          />

          <Button
            fullWidth
            size="medium"
            type="submit"
            variant="contained"
            disabled={loading}
            sx={{
              fontWeight: 700,
              textTransform: "none",
              py: 1,
              borderRadius: 2,
              backgroundImage: "linear-gradient(90deg, #2e7d32, #66bb6a)",
              boxShadow: "0 6px 16px rgba(46,125,50,0.25)",
              "&:hover": {
                backgroundImage: "linear-gradient(90deg, #1b5e20, #43a047)",
                boxShadow: "0 8px 20px rgba(46,125,50,0.35)"
              }
            }}
          >
            {loading ? "Registrando..." : "Registrarse"}
          </Button>
        </Box>

        <RegisterDivider />

        <RegisterSocial />

        {/* Pie de términos y condiciones */}
        <Typography
          variant="caption"
          sx={{
            display: "block",
            textAlign: "center",
            mt: 1,
            color: "text.secondary"
          }}
        >
          Al registrarte aceptas nuestros{" "}
          <Link href="/terminos" target="_blank">Términos</Link> y{" "}
          <Link href="/privacidad" target="_blank">Política de Privacidad</Link>.
        </Typography>
 
    </div>
  );
}

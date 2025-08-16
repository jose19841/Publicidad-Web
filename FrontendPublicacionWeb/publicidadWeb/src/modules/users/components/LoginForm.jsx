import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";
import Typography from "@mui/material/Typography";
import useLoginForm from "../hooks/useLoginForm";
import LoginHeader from "./LoginHeader";
import LoginFields from "./LoginFields";
import LoginDivider from "./LoginDivider";
import LoginSocial from "./LoginSocial";

export default function LoginForm({ onSuccess, disableNavigate = false }) {
  const {
    email, setEmail,
    password, setPassword,
    rememberMe, setRememberMe,
    showPassword, setShowPassword,
    loading, error,
    onSubmit,
    openGooglePopup
  } = useLoginForm({ onSuccess, disableNavigate });

  return (
<>
<div className="">
        <LoginHeader />

        {/* Form */}
        <Box component="form" onSubmit={onSubmit} sx={{ display: "grid", gap: 2.5 }}>
          <LoginFields
            email={email}
            setEmail={setEmail}
            password={password}
            setPassword={setPassword}
            showPassword={showPassword}
            setShowPassword={setShowPassword}
            rememberMe={rememberMe}
            setRememberMe={setRememberMe}
            error={error}
          />

          {/* CTA principal */}
          <Button
            fullWidth
            size="large"
            type="submit"
            variant="contained"
            disabled={loading}
            sx={{
              mt: 1,
              py: 1.2,
              fontWeight: 700,
              borderRadius: 2,
              textTransform: "none",
              backgroundImage: "linear-gradient(90deg, #1976d2, #42a5f5)",
              boxShadow: "0 6px 16px rgba(25,118,210,0.25)",
              "&:hover": {
                backgroundImage: "linear-gradient(90deg, #1565c0, #1e88e5)",
                boxShadow: "0 8px 20px rgba(25,118,210,0.35)"
              }
            }}
          >
            {loading ? "Ingresando..." : "Ingresar"}
          </Button>

          {/* Enlaces secundarios */}
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mt: -0.5
            }}
          >
            <Link href="/recuperar-contraseña" variant="body2" sx={{ color: "text.secondary" }}>
              ¿Olvidaste tu contraseña?
            </Link>
            <Link href="/register" variant="body2" sx={{ fontWeight: 600 }}>
              Crear cuenta
            </Link>
          </Box>
        </Box>

        <LoginDivider />

        {/* Social login */}
        <LoginSocial openGooglePopup={openGooglePopup} />

        {/* Pie sutil */}
        <Typography
          variant="caption"
          sx={{ display: "block", textAlign: "center", mt: 2, color: "text.secondary" }}
        >
          Al continuar aceptas nuestros <Link href="/terminos">Términos</Link> y{" "}
          <Link href="/privacidad">Política de Privacidad</Link>.
        </Typography>
        </div>
  </>
  );
}

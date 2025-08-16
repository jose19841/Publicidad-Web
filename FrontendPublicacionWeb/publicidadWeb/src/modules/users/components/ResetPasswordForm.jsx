import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Stack from "@mui/material/Stack";
import Alert from "@mui/material/Alert";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import useResetPasswordForm from "../hooks/useResetPassword";
import "../styles/RecoverPassword.css"; 

export default function ResetPasswordForm({ token }) {
  const {
    newPassword,
    setNewPassword,
    confirmPassword,
    setConfirmPassword,
    showPass,
    setShowPass,
    showConfirm,
    setShowConfirm,
    passwordError,
    confirmError,
    loading,
    error,
    successMsg,
    submit,
  } = useResetPasswordForm({ token });

  return (
    <form onSubmit={submit} noValidate className="auth-form">
      <Stack spacing={2}>
        {successMsg && (
            <div className="pb-3">

          <Alert
            icon={<CheckCircleIcon fontSize="inherit" />}
            severity="success"
            >
            {successMsg} (redirigiendo…)
          </Alert>
              </div>
        )}

        {error && <Alert severity="error">{error}</Alert>}

        {!successMsg && (
          <>
            <TextField
              label="Nueva contraseña"
              type={showPass ? "text" : "password"}
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              error={Boolean(passwordError)}
              helperText={passwordError}
              fullWidth
              disabled={loading}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPass((p) => !p)}
                      edge="end"
                    >
                      {showPass ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            <TextField
              label="Repetir contraseña"
              type={showConfirm ? "text" : "password"}
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              error={Boolean(confirmError)}
              helperText={confirmError}
              fullWidth
              disabled={loading}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowConfirm((p) => !p)}
                      edge="end"
                    >
                      {showConfirm ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              fullWidth
              className="auth-button"
            >
              {loading ? "Procesando..." : "Restablecer contraseña"}
            </Button>
          </>
        )}
      </Stack>
    </form>
  );
}

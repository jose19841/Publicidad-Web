import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useLogin from "../hooks/useLogin";
import authService from "../services/authService";
import { useAuth } from "../../../context/AuthContext";

export default function useLoginForm({ onSuccess, disableNavigate }) {
  const { login } = useAuth();
  const [email, setEmail]           = useState("");
  const [password, setPassword]     = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { loading, error, handleLogin } = useLogin();
  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    const user = await handleLogin(email, password, rememberMe);
    if (!user) return;

    if (disableNavigate || onSuccess) {
      onSuccess?.(user);
      return;
    }

    if (user.role === "ADMIN")      navigate("/admin/dashboard");
    else if (user.role === "USER")  navigate("/publicaciones");
    else                            navigate("/");
  };

  const openGooglePopup = () => {
    const w = 520, h = 640;
    const dualScreenLeft = window.screenLeft ?? window.screenX ?? 0;
    const dualScreenTop  = window.screenTop  ?? window.screenY ?? 0;
    const left = dualScreenLeft + (window.outerWidth - w) / 2;
    const top  = dualScreenTop + (window.outerHeight - h) / 2;

    const popup = window.open(
      "http://localhost:8080/oauth2/authorization/google",
      "google_oauth",
      `toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=${w},height=${h},top=${top},left=${left}`
    );
    if (!popup) return;

    const timer = setInterval(async () => {
      if (popup.closed) {
        clearInterval(timer);
        return;
      }
      try {
        const me = await authService.getProfile();
        if (me) {
          clearInterval(timer);
          try { popup.close(); } catch {}
          login(me);

          if (disableNavigate || onSuccess) {
            onSuccess?.(me);
            return;
          }
          if (me.role === "ADMIN")      navigate("/admin/dashboard");
          else if (me.role === "USER")  navigate("/publicaciones");
          else                          navigate("/");
        }
      } catch {
        // No hay sesión todavía
      }
    }, 800);
  };

  return {
    email, setEmail,
    password, setPassword,
    rememberMe, setRememberMe,
    showPassword, setShowPassword,
    loading, error,
    onSubmit,
    openGooglePopup
  };
}

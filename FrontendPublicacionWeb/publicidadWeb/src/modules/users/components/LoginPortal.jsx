import ReactDOM from "react-dom";
import LoginForm from "../components/LoginForm";
import useLoginPortal from "../hooks/useLoginPortal";
import "../styles/LoginPortal.css";

export default function LoginPortal() {
  const { loginOpen, closePortal, handleSuccess, handleBackdropClick } = useLoginPortal();

  if (!loginOpen) return null;

  return ReactDOM.createPortal(
    <div
      role="dialog"
      aria-modal="true"
      aria-label="Formulario de inicio de sesión"
      className="login-portal-backdrop"
      onClick={handleBackdropClick}
    >
      <div className="login-portal-container">
        <div className="login-portal-header"></div>

        <div className="login-portal-body">
          <LoginForm disableNavigate onSuccess={handleSuccess} />
          <button
            type="button"
            className="ct-btn-red login-portal-cancel"
            onClick={closePortal}
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>,
    document.getElementById("modal-root")
  );
}

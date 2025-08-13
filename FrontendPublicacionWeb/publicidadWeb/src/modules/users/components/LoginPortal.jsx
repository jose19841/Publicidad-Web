import ReactDOM from "react-dom";
import { useAuth } from "../../../context/AuthContext";
import LoginForm from "../components/LoginForm";

export default function LoginPortal() {
  const { loginOpen, setLoginOpen, login } = useAuth();
  if (!loginOpen) return null;

  return ReactDOM.createPortal(
    <div
      role="dialog"
      aria-modal="true"
      aria-label="Formulario de inicio de sesión"
      onClick={(e) => e.target === e.currentTarget && setLoginOpen(false)}
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,.5)",
        display: "grid",
        placeItems: "center",
        zIndex: 9999,
        padding: 16,
      }}
    >
      <div
        style={{
          width: "min(460px,100%)",
          background: "white",
          borderRadius: 12,
          border: "1px solid var(--ct-border, #eee)",
          overflow: "hidden",
        }}
      >
        <div
          style={{
            padding: 16,
            borderBottom: "1px solid var(--ct-border, #eee)",
          }}
        >
          
        </div>

        <div style={{ padding: 16 }}>
          <LoginForm
            disableNavigate
            onSuccess={(user) => {
              login(user); 
              setLoginOpen(false); 
            }}
          />
          <button
            type="button"
            className="ct-btn-red"
            onClick={() => setLoginOpen(false)}
            style={{ marginTop: 8 }}
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>,
    document.getElementById("modal-root")
  );
}

import { useAuth } from "../../../context/AuthContext";

export default function useLoginPortal() {
  const { loginOpen, setLoginOpen, login } = useAuth();

  const closePortal = () => setLoginOpen(false);

  const handleSuccess = (user) => {
    login(user);
    closePortal();
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      closePortal();
    }
  };

  return {
    loginOpen,
    closePortal,
    handleSuccess,
    handleBackdropClick,
  };
}

// modules/users/hooks/useRequireAuth.js
import { useAuth } from "../../../context/AuthContext"; // <--- misma ruta del Provider

export default function useRequireAuth() {
  const { requireAuth } = useAuth();

  const withAuth = (fn) => async (...args) => {
  const ok = await requireAuth();
  if (!ok) return;
  return fn?.(...args);
};

  return { withAuth };
}

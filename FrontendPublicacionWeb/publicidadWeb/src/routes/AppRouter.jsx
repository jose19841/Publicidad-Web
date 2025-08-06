import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from '../modules/users/pages/LoginPage';
import RegisterPage from "../modules/users/pages/RegisterPage";

const AppRouter = () => {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
};

export default AppRouter;

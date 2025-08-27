import apiClient from "../../../services/apiClient";

const login = async (credentials) => {
  const response = await apiClient.post("/auth/login", credentials);
  return response.data;
};

const register = async (userData) => {
  const response = await apiClient.post("/user/userRegister", userData);
  return response.data;
};

const socialLogin = (provider) => {
  window.location.href = `/auth/oauth2/${provider}`;
};

const getProfile = async () => {
  const response = await apiClient.get("/auth/me", { withCredentials: true });
  return response.data;
};
const logout = async () => {
  await apiClient.post("/auth/logout", {}, { withCredentials: true });
};
export default { login, register, socialLogin, getProfile, logout };

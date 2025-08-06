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

export default { login, register, socialLogin };

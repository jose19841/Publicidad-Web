// src/modules/users/services/userService.js
import apiClient from "../../../services/apiClient";

export const getUsers = async () => {
  const { data } = await apiClient.get("/user/getUsers");
  return data;
};

export const changeStatus = async (id) => {
  await apiClient.patch(`/user/${id}/status`);
};

export const updateUser = async (id, userData) => {
  const { data } = await apiClient.put(`/user/${id}`, userData);
  return data;
};

export const registerUser = async (form) => {
  const { data } = await apiClient.post("/user/userRegister", form);
  return data;
};